package fastdfs;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * @ClassName FastDFSUtils
 * @Description FastDFS工具类
 * @author litao
 */
public class FastDFSUtils implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -4462272673174266738L;
    private static TrackerClient trackerClient;
    private static TrackerServer trackerServer;
    private static StorageClient1 storageClient1;

    static {
        try {
            //clientGloble读配置文件 H:\U盘备份\Test1\src\main\resources\fdfs_client.conf
           // ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
            //ClientGlobal.init(resource.getClassLoader().getResource("fdfs_client.conf").getPath());
            ClientGlobal.init("H:\\U盘备份\\Test1\\src\\main\\resources\\fdfs_client.conf");
            //trackerclient
            trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            //storageclient
            storageClient1 = new StorageClient1(trackerServer,null); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * fastDFS文件上传
     * @param file 上传的文件 FastDFSFile
     * @return String 返回文件的绝对路径
     */
    public static String uploadFile(FastDFSFile file){
        String path = null;
        try {
            //文件扩展名
            String ext = FilenameUtils.getExtension(file.getName());
            //mata list是表文件的描述
            NameValuePair[] mata_list = new NameValuePair[3];
            mata_list[0] = new NameValuePair("fileName",file.getName());
            mata_list[1] = new NameValuePair("fileExt",ext);
            mata_list[2] = new NameValuePair("fileSize",String.valueOf(file.getSize()));
            path = storageClient1.upload_file1(file.getContent(), ext, mata_list);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return path;
    }
    @Test
    public void testDownload() {
        try {
            byte[] b = storageClient1.download_file("group1", "M00/00/00/wKgfeVs9b8OAFRg4AAJph3igwH0816.jpg");
            System.out.println(b);
            IOUtils.write(b, new FileOutputStream("D:/"+ UUID.randomUUID().toString()+".jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpload() {

        try {
            String local_filename = "F:\\壁纸\\272fc4a8082f0dfa63ccc75f1b6f8cad.jpg";
            NameValuePair nvp [] = new NameValuePair[]{
                    new NameValuePair("age", "18"),
                    new NameValuePair("sex", "male")
            };
            String fileIds[] = storageClient1.upload_file(local_filename, "jpg", nvp);

            System.out.println(fileIds.length);
            System.out.println("组名：" + fileIds[0]);
            System.out.println("路径: " + fileIds[1]);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetFileInfo(){
        try {

            FileInfo fi = storageClient1.get_file_info("group1", "M00/00/00/wKgfeVs9jHSAbbKiAAF9KMsAdUk213.jpg");
            System.out.println(fi.getSourceIpAddr());
            System.out.println(fi.getFileSize());
            System.out.println(fi.getCreateTimestamp());
            System.out.println(fi.getCrc32());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetFileMate(){
        try {
            NameValuePair nvps [] = storageClient1.get_metadata("group1", "M00/00/00/wKgfeVs9jHSAbbKiAAF9KMsAdUk213.jpg");
            for(NameValuePair nvp : nvps){
                System.out.println(nvp.getName() + ":" + nvp.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testDelete(){
        try {

            int i = storageClient1.delete_file("group1", "M00/00/00/wKgRcFV_08OAK_KCAAAA5fm_sy874.conf");
            System.out.println( i==0 ? "删除成功" : "删除失败:"+i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
