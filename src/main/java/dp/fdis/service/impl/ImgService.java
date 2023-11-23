package dp.fdis.service.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import dp.fdis.dto.ImgDTO;
import dp.fdis.persistance.mapper.IImgMapper;
import dp.fdis.service.IImgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImgService implements IImgService {

    private final IImgMapper imgMapper;

    @Value("${nAkey.api.key}")
    private String accessKey;

    @Value("${nSkey.api.key}")
    private String secretKey;


    @Override
    public ImgDTO upLoadImg(MultipartFile multipartFile, ImgDTO pDTO) throws Exception {


        // S3 client
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();

        String bucketName = "imgdb"+"/img";

// upload local file

        String tourSeq = pDTO.getTourSeq();
        String tourDay = pDTO.getTourDay();
        String placeSeq = pDTO.getPlaceSeq();
        String userId = pDTO.getUserId();

        log.info("tourSeq : " + tourSeq);
        log.info("tourDay : " + tourDay);
        log.info("placeSeq : " + placeSeq);
        log.info("userId : " + userId);

        String originalName = multipartFile.getOriginalFilename();
        String ext = originalName.substring(originalName.lastIndexOf(".") + 1);

        log.info("ext : " + ext);


        String objectName = tourSeq + "_" + tourDay + "_" + placeSeq + "_" + userId + "." + ext;  // 업로드 할 때 파일 이름
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());


        log.info("objectName : " + objectName);


        String imgType = "image/" + ext;

        objMeta.setContentType(imgType);

        ImgDTO rDTO = new ImgDTO();

        try {
            s3.putObject( new PutObjectRequest(bucketName, objectName, multipartFile.getInputStream(), objMeta).withCannedAcl(CannedAccessControlList.PublicRead));
            log.info("Object " + objectName + " has been created");

            rDTO.setImgURL("https://kr.object.ncloudstorage.com/" + bucketName + "/" + objectName);

        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }

        return rDTO;

    }

    @Override
    public void deleteImg(ImgDTO pDTO) throws Exception {
        // S3 client
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();

        String bucketName = "imgdb"+"/img";
        String objectName = pDTO.getUploadFileName();

// delete object
        try {
            s3.deleteObject(bucketName, objectName);
            System.out.format("Object %s has been deleted.\n", objectName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    @Override
    public void insertTourImg(ImgDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".insertTourImg start!");

        imgMapper.insertTourImg(pDTO);

    }

    @Override
    public ImgDTO imgYn(ImgDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".imgYn start!");

        return imgMapper.imgYn(pDTO);
    }

}
