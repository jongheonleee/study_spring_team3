package com.example.shop2.service.item;

import com.example.shop2.entity.item.ItemImg;
import com.example.shop2.repository.item.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    // 파일 이미지 저장할 경로 iv로 설정
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    // 상품 이미지 repository 주입
    @Autowired
    private final ItemImgRepository itemImgRepository;

    // 파일 처리 서비스 주입
    @Autowired
    private final FileService fileService;


    // 1. 이미지 등록, 파라미터 : ItemImg 엔티티, MultiPartFile
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{
        // 파일 이름 조회
        String oriImgName = itemImgFile.getOriginalFilename();
        // 이미지 이름 초기화
        String imgName = "";
        // 이미지 url 초기화
        String imgUrl = "";

        // 파일 업로드, 파일 이미지가 존재하는 경우에만 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }


        // 상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

}
