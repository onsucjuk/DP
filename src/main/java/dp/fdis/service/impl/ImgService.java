package dp.fdis.service.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import dp.fdis.dto.ImgDTO;
import dp.fdis.service.IImgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImgService implements IImgService {

    @Value("${nAkey.api.key}")
    private String accessKey;

    @Value("${nSkey.api.key}")
    private String secretKey;


    @Override
    public void upLoadImg(ImgDTO pDTO) throws Exception {


        // S3 client
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();

        String bucketName = "imgdb";

// upload local file

        String objectName = pDTO.getUploadFileName(); // 업로드 할 때 파일 이름
        String filePath = pDTO.getUploadFilePath(); // 업로드 되는 파일 위치[ FilePaht + "/" + OriginalFileName ]

        try {
            s3.putObject(bucketName, objectName, new File(filePath));
            System.out.format("Object %s has been created.\n", objectName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteImg(ImgDTO pDTO) throws Exception {
        // S3 client
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();

        String bucketName = "imgdb";
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


}
