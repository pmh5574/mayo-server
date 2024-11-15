package com.mayo.server.chef.app.service;

import com.mayo.server.chef.adapter.in.web.MyPageRequest;
import com.mayo.server.chef.adapter.out.persistence.ProfileUpdateResponse;
import com.mayo.server.chef.app.port.in.*;
import com.mayo.server.chef.app.port.out.ChefQueryOutputPort;
import com.mayo.server.chef.app.port.out.MyPageOutputPort;
import com.mayo.server.chef.domain.model.*;
import com.mayo.server.common.Constants;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.enums.VerifyCode;
import com.mayo.server.common.exception.DuplicateException;
import com.mayo.server.common.exception.InvalidRequestException;
import com.mayo.server.common.exception.NotEqualException;
import com.mayo.server.common.exception.NotFoundException;
import com.mayo.server.common.utility.DateUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mayo.server.common.Constants.yyyy_MM_DD_HH_mm_ss;

@Service
@RequiredArgsConstructor
public class MyPageCommandUseCaseService implements MyPageCommandUseCase {

    private final ChefQueryOutputPort outputPort;
    private final MyPageInputPort inputPort;
    private final S3InputPort s3InputPort;
    private final MyPageOutputPort myPageOutputPort;

    @Override
    @Transactional
    public void updateMyPage(MyPageRequest.ChefInfoRequest req, Long id) {

        boolean isPhone = false;
        boolean isEmail = false;

        if (Objects.nonNull(req.phone())) {

            ChefPhone chefPhone = outputPort.getPhoneVerifyByType(req.phone(), VerifyCode.MY_PAGE.toString());
            if(Objects.isNull(chefPhone)){
                throw new NotFoundException(ErrorCode.CHEF_PHONE_NOT_FOUND);
            }

            Chef isDuplicatedChef = outputPort.getChefByPhone(req.phone());
            if(Objects.nonNull(isDuplicatedChef)) {
                throw new DuplicateException(ErrorCode.CHEF_DUPLICATED_PHONE);
            }

            if(-5 > (DateUtility.getNowDiffTime(chefPhone.getCreatedAt(), yyyy_MM_DD_HH_mm_ss) / 60)) {
                throw new InvalidRequestException(
                        ErrorCode.VERIFY_EXPIRED,
                        ErrorCode.VERIFY_EXPIRED.getMessage()
                );
            }

            if (!req.phoneAuthNum().equals(chefPhone.getAuthNum())) {
                throw new NotEqualException(ErrorCode.PHONE_AUTH_NUMBER_NOT_EQUALS);
            }

            isPhone = true;

        }

        if (Objects.nonNull(req.email())) {

            ChefEmail chefEmail = outputPort.getEmailVerifyByType(req.email(), VerifyCode.MY_PAGE.toString());
            if(Objects.isNull(chefEmail)){
                throw new NotFoundException(ErrorCode.CHEF_EMAIL_NOT_FOUND);
            }

            if(-5 > (DateUtility.getNowDiffTime(chefEmail.getCreatedAt(), yyyy_MM_DD_HH_mm_ss) / 60)) {
                throw new InvalidRequestException(
                        ErrorCode.VERIFY_EXPIRED,
                        ErrorCode.VERIFY_EXPIRED.getMessage()
                );
            }

            Chef isDuplicatedChef = outputPort.getChefByEmail(req.email());
            if(Objects.nonNull(isDuplicatedChef)) {
                throw new DuplicateException(ErrorCode.CHEF_DUPLICATED_EMAIL);
            }

            if (!req.emailAuthNum().equals(chefEmail.getAuthNum())) {
                throw new NotEqualException(ErrorCode.EMAIL_AUTH_NUMBER_NOT_EQUALS);
            }

            isEmail = true;

        }

        if (isPhone && isEmail) {
            inputPort.updateMyPage(req.name(), req.birthday(), req.email(), req.phone(), id);
        }

        if (isPhone && !isEmail) {
            inputPort.updateMyPage(req.name(), req.birthday(), req.phone(), id);
        }

        if (!isPhone && isEmail) {
            inputPort.updateMyPage(req.name(), req.birthday(), req.email(), id, true);
        } else {

            inputPort.updateMyPage(req.name(), req.birthday(), id);
        }

    }

    @Override
    @Transactional
    public ProfileUpdateResponse updateProfile(MyPageRequest.ProfileRequest req, Long id) {

        String createdAt = DateUtility.getUTC0String(yyyy_MM_DD_HH_mm_ss);

        ChefInformation chefInformation = myPageOutputPort.getChefInformation(id);

        if(Objects.nonNull(chefInformation)) {

            chefInformation.setChefInfoAdditional(req.additionalInfo());
            chefInformation.setChefInfoDescription(req.description());
            chefInformation.setChefInfoExperience(req.experience());
            chefInformation.setChefInfoRegion(req.activeRegion());
            chefInformation.setChefInfoIntroduce(req.comment());
            chefInformation.setHopePay(req.hopePay());
            chefInformation.setMinServiceTime(req.minServiceTime());

            inputPort.postChefInfo(
                    chefInformation
            );
        } else {
            inputPort.postChefInfo(
                    ChefInformation
                            .builder()
                            .chefId(id)
                            .chefInfoAdditional(req.additionalInfo())
                            .chefInfoDescription(req.description())
                            .chefInfoExperience(req.experience())
                            .chefInfoRegion(req.activeRegion())
                            .chefInfoIntroduce(req.comment())
                            .hopePay(req.hopePay())
                            .minServiceTime(req.minServiceTime())
                            .build()
            );
        }

        if(!req.serviceList().isEmpty()){

            inputPort.deleteServiceList(id);

            inputPort.postServiceList(id, req.serviceList(), createdAt);
        }

        if(!req.hashtags().isEmpty()) {

            List<ChefHashTag> hashTags = myPageOutputPort.getChefHashTags(id);
            if(!hashTags.isEmpty()) {

                inputPort.deleteByHashTags(id);
            }

            inputPort.postHashTag(
                    id,
                    req.hashtags()
            );

        }
        List<TransformedImageDto> portFolioPreSignedUrls = postPortFolio(
                req, id
        );
        List<TransformedImageDto> licensePreSignedUrls = postLicense(
                req, id
        );
        return new ProfileUpdateResponse(
                portFolioPreSignedUrls,
                licensePreSignedUrls
        );

    }

    @Transactional(propagation = Propagation.REQUIRED)
    protected List<TransformedImageDto> postPortFolio(
            MyPageRequest.ProfileRequest req,
            Long id
    ) {
        List<TransformedImageDto> portFolioPreSignedUrls = new ArrayList<>();
        List<TransformedImageDto> portFolioCloudSignedUrls = new ArrayList<>();
        List<ChefPortfolio> licenses = myPageOutputPort.getChefPortPolio(id);
        if(!licenses.isEmpty()){

            s3InputPort.deleteS3Objects(id, Constants.MY_PAGE_PORTFOLIO);

            inputPort.deletePortfolio(id);
        }
        if(!req.portfolio().isEmpty()) {

            portFolioPreSignedUrls = req.portfolio().stream().map(
                    image -> {
                        TransformedImageDto preSignedDto = s3InputPort.getPreSignedUrl(
                                Constants.MY_PAGE_PORTFOLIO,
                                id,
                                image.getId(),
                                image.getKey()
                        );
                        TransformedImageDto cloudSignedDto = s3InputPort.getCloudFrontSignedUrl(
                                image.getId(),
                                s3InputPort.getImagePrefix(
                                        Constants.MY_PAGE_PORTFOLIO,
                                        id, image.getId(), image.getKey()
                                )

                        );
                        portFolioCloudSignedUrls.add(cloudSignedDto);

                        return preSignedDto;
                    }
            ).toList();
            inputPort.postPortfolio(
                    id,
                    portFolioCloudSignedUrls
            );
        }

        return portFolioPreSignedUrls;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    protected List<TransformedImageDto> postLicense(
            MyPageRequest.ProfileRequest req,
            Long id
    ) {
        List<TransformedImageDto> licensePreSignedUrls = new ArrayList<>();
        List<TransformedImageDto> licenseCloudSignedUrls = new ArrayList<>();

        List<ChefLicense> licenses = myPageOutputPort.getChefLicense(id);
        if(!licenses.isEmpty()){

            s3InputPort.deleteS3Objects(id, Constants.MY_PAGE_LICENSE);

            inputPort.deleteLicense(id);

        }
        if(!req.license().isEmpty()) {

            licensePreSignedUrls = req.license().stream().map(
                    image -> {

                        TransformedImageDto preSignedDto = s3InputPort.getPreSignedUrl(
                                Constants.MY_PAGE_LICENSE,
                                id,
                                image.getId(),
                                image.getKey()
                        );

                        TransformedImageDto cloudSignedDto = s3InputPort.getCloudFrontSignedUrl(
                                image.getId(),
                                s3InputPort.getImagePrefix(
                                        Constants.MY_PAGE_LICENSE,
                                        id, image.getId(), image.getKey()
                                )

                        );

                        licenseCloudSignedUrls.add(cloudSignedDto);

                        return preSignedDto;
                    }
            ).toList();

            inputPort.postLicense(
                    id,
                    licenseCloudSignedUrls
            );

        }

        return licensePreSignedUrls;
    }
}
