package com.web.controller;

import com.web.domain.Product;
import com.web.domain.ProductFiles;
import com.web.repository.ProductFilesRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/files")
public class FileController {
    @Autowired
    private ProductFilesRepository productFilesRepository;

    @GetMapping("/getList")
    @ResponseBody
    public List<ProductFiles> getList(Long pno){
        Product product=new Product();
        product.setPno(pno);
        return getListByproduct(product);
    }

    @PostMapping(value = "/upload")
    @ResponseBody
    public List<ProductFiles> uploadAjaxPost(MultipartFile[] uploadFile) {

        List<ProductFiles> list = new ArrayList<>();
        String uploadFolder = "C:\\upload\\product\\";

        String uploadFolderPath = getFolder();

        File uploadPath = new File(uploadFolder, uploadFolderPath);

        if (uploadPath.exists() == false) {
            uploadPath.mkdirs();
        }

        for (MultipartFile multipartFile : uploadFile) {
            ProductFiles productFiles = new ProductFiles();
            String uploadFileName = multipartFile.getOriginalFilename();
            productFiles.setFileName(uploadFileName);

            UUID uuid = UUID.randomUUID();
            uploadFileName = uuid.toString() + "_" + uploadFileName;
            try {
                File saveFile = new File(uploadPath, uploadFileName);
                multipartFile.transferTo(saveFile);

                productFiles.setUuid(uuid.toString());
                productFiles.setUploadUrl(uploadFolderPath);
  /*              //썸네일 생성
                int idx_fileName=uploadFileName.indexOf(".")+1;
                Thumbnails.of(saveFile).size(460,571).outputFormat(uploadFileName.substring(idx_fileName,uploadFileName.length())).toFile(new File(uploadPath,"s_"+uploadFileName));
*/
                //filesRepository.save(files);
                list.add(productFiles);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } // end for
        return list;
    }

    @GetMapping("/modify")
    public String modify(String uuid,boolean result){
        ProductFiles productFiles=productFilesRepository.findProductFilesByUuid(uuid);
        productFiles.setMainPic(result);
        productFilesRepository.save(productFiles);
        return "success";
    }

    @GetMapping("/display")
    @ResponseBody
    public ResponseEntity<byte[]> getFile(String fileName) {
        File file = new File("c:\\upload\\product\\" + fileName);
        ResponseEntity<byte[]> result = null;
        try {
            HttpHeaders header = new HttpHeaders();

            header.add("Content-Type", java.nio.file.Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    @PostMapping("/delete")
    @ResponseBody
    public String deleteFile(String fileName) {
        String uploadFolder="C:\\upload\\product\\";

        try {
            String uploadFileName= (URLDecoder.decode(fileName, "UTF-8")).toString().replace("/","\\");
            File originFile= new File(uploadFolder + uploadFileName);
            originFile.delete();

/*            //썸네일
            String largeFileName = originFile.getAbsolutePath().replace("s_", "");
            File imageFile = new File(largeFileName);
            imageFile.delete();*/
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "deleted";
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public String deleteFileDB(String fileName, String type,Long fno) {
        String uploadFolder="C:\\upload\\product\\";

        try {
            String uploadFileName= (URLDecoder.decode(fileName, "UTF-8")).toString().replace("/","\\");
            System.out.println("☆★☆★☆★☆★☆★"+uploadFileName);
            File originFile= new File(uploadFolder + uploadFileName);
            originFile.delete();
/*            //썸네일
            String largeFileName = originFile.getAbsolutePath().replace("s_", "");
            System.out.println("☆★☆★☆★☆★☆★"+largeFileName);
            File imageFile = new File(largeFileName);
            imageFile.delete();*/

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        productFilesRepository.deleteById(fno);
        return "deleted";
    }

    private String getFolder() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String str = sdf.format(date);
        return str.replace("-", File.separator);
    }

    private List<ProductFiles> getListByproduct(Product product) throws RuntimeException {
        return productFilesRepository.getRepliesofProduct(product);
    }
}
