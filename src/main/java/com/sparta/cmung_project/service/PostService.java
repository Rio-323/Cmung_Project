package com.sparta.cmung_project.service;

import com.sparta.cmung_project.dto.*;
import com.sparta.cmung_project.exception.CustomException;
import com.sparta.cmung_project.exception.ErrorCode;
import com.sparta.cmung_project.model.Category;
import com.sparta.cmung_project.model.Image;
import com.sparta.cmung_project.model.Member;
import com.sparta.cmung_project.model.Post;
import com.sparta.cmung_project.repository.CategoryRepository;
import com.sparta.cmung_project.repository.ImageRepository;
import com.sparta.cmung_project.repository.PostRepository;
import com.sparta.cmung_project.s3.S3Service;
import com.sparta.cmung_project.util.Chrono;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ImageRepository imgRepository;
    private final CategoryRepository categoryRepository;
    private final S3Service s3Service;


    // 게시글 생성
    @Transactional
    public GlobalResDto<PostResponseDto> createPost(PostRequestDto postRequestDto, List<MultipartFile> file, Member member){
        log.info("createPost() 호출");

        List<Image> imgs = new ArrayList<>();

        // 카테고리 검색
        Optional<Category> categoryOpt = categoryRepository.findByName(postRequestDto.getCategory());

        // 포스트 생성
        Post post = null;
        if(categoryOpt.isPresent()) {
            log.info("카테고리 존재");
            log.info("Boolean : " + String.valueOf(categoryOpt.isPresent()));
            log.info("get() : " + String.valueOf(categoryOpt.get()));
            log.info("Category Name : " + String.valueOf(categoryOpt.get().getName()));
            post = new Post(postRequestDto, categoryOpt.get(), member);
        } else {
            log.info("카테고리 생성");
            Category category = new Category(postRequestDto.getCategory());
            categoryRepository.save(category);
            post = new Post(postRequestDto, category, member);
        }
        
        // MultipartFile Null 체크
        if(file != null) {
            log.info("파일이 Null이 아닙니다.");
            // 이미지 파일 처리
            for (MultipartFile multipartFile : file) {
                // 이미지 저장
                Image img = imgRepository.save(new Image(s3Service.uploadFile(multipartFile), post));
                log.info("이미지 저장 : " + img.getImage());
                // 이미지 리스트에 추가
                imgs.add(img);
            }
        } else {
            log.info("파일이 Null 입니다.");
            log.info("이미지 저장 과정을 생략합니다.");
        }

        // 포스트 DB 저장
        postRepository.save(post);

        // 반환 DTO 작성
        PostResponseDto postResponseDto = new PostResponseDto(post);

        // 이미지 가져오기
        List<String> imgList = new ArrayList<>();
        for (Image img : imgs) {
            imgList.add(img.getImage());
        }

        // 반환 DTO 이미지 리스트 설정
        postResponseDto.setImgs(imgList);

        // DTO 반환
        return GlobalResDto.success(postResponseDto,"게시글 작성이 완료되었습니다.");
    }


    // 게시글 최신순으로 가져오기
    @Transactional(readOnly = true)
    public GlobalResDto<?> allPost(Pageable pageable){
        // 포스트 최신순으로 가져오기
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        // 포스트 반환 DTO 리스트 작성
        List<AllPostResponseDto> allPostResponseDtos = new ArrayList<>();

        for(Post p : posts){
            // 이미지 리스트 작성
            List<String> imgList = new ArrayList<>();

            // 이미지 리스트에 이미지 추가
            for(Image img : p.getImage()){
                imgList.add(img.getImage());
            }

            // DTO 리스트에 DTO 추가
            allPostResponseDtos.add(new AllPostResponseDto(p, imgList));
        }

        // DTO 반환
        return GlobalResDto.success(allPostResponseDtos,"조회를 성공하였습니다.");
    }

    
    // 게시글 삭제
    @Transactional
    public GlobalResDto<PostResponseDto> delPost(Long postId, Member member){
        // 게시글 가져오기
        // postId와 member를 함께 사용한다.
        Post post = postRepository.findByIdAndMember(postId, member);

        // 가져온 게시글 존재 유무 검사
        if (post == null) return GlobalResDto.fail("게시글 삭제에 실패했습니다. 다시 시도해주세요.");

        // 게시글 삭제하기
        postRepository.deleteById(post.getId());

        // DTO 반환
        return GlobalResDto.success(null,"삭제 되었습니다.");
    }

    
    // 게시글 수정
    @Transactional
    public GlobalResDto<PostResponseDto> modifyPost(Long postId, List<MultipartFile> file, PostRequestDto postRequestDto, Member member){
        log.info("modifyPost() 호출");

        // 게시글 가져오기
        Post post = postRepository.findByIdAndMember(postId, member);

        // 게시글 존재 유무 검사
        if (post == null) return GlobalResDto.fail("게시글 수정에 실패했습니다. 다시 시도해주세요.");

        // 게시글에서 이미지 리스트 가져오기
        List<Image> imageList = post.getImage();

        // 기존 이미지 리스트 삭제
        for (Image i : imageList) {
            imgRepository.delete(i);
        }

        // 이미지 리스트 작성
        List<Image> imgs = new ArrayList<>();

        // MultipartFile Null 체크
        if(file != null) {
            log.info("파일이 Null이 아닙니다.");
            // S3에 이미지 저장
            for (MultipartFile multipartFile : file) {
                // S3에 이미지 저장
                // 이미지 생성
                Image img = imgRepository.save(new Image(s3Service.uploadFile(multipartFile), post));
                // 이미지 리스트에 이미지 추가
                imgs.add(img);
            }
        } else {
            log.info("파일이 Null 입니다.");
            log.info("이미지 저장 과정을 생략합니다.");
        }

        // 카테고리 검색
        Optional<Category> categoryOpt = categoryRepository.findByName(postRequestDto.getCategory());

        // 게시글 수정
        post.update(postRequestDto, categoryOpt.get());

        // 이미지 리스트 작성
        List<String> imgList = new ArrayList<>();

        // 이미지 리스트에 이미지 추가
        for (Image img : imgs) {
            imgList.add(img.getImage());
        }

        // 수정된 게시글 DB에 저장
        // 반환 DTO 작성
        PostResponseDto postResponseDto = new PostResponseDto(postRepository.save(post));

        // DTO에 이미지 리스트 설정
        postResponseDto.setImgs(imgList);

        // DTO 반환
        return GlobalResDto.success(postResponseDto,"수정이 완료 되었습니다.");
    }

    // 게시글 검색
    public GlobalResDto<?> searchPost(String searchKeyword) {

        if(searchKeyword.length () < 1) {
            throw new CustomException ( ErrorCode.SearchKeywordLengthError );
        }

        List<Post> postList = postRepository.findAllByTitleContainingOrContentContainingOrLocalContainingOrderByCreatedAtDesc ( searchKeyword, searchKeyword, searchKeyword );

        List<GetAllPostDto> getAllPostDtoList = getAllPost ( postList );

        return GlobalResDto.success ( getAllPostDtoList, null );
    }

    // 게시글 리스트를 DTO로 만든다.
    public List<GetAllPostDto> getAllPost(List<Post> postList) {
        List<GetAllPostDto> getAllPostDtoList = new ArrayList<> ();

        for(Post post : postList) {
            String time = Chrono.timesAgo ( post.getCreatedAt() );
            GetAllPostDto getAllPostDto = GetAllPostDto.getAllPostDto ( post, time );
            getAllPostDtoList.add ( getAllPostDto );
        }

        return getAllPostDtoList;
    }

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public GlobalResDto<PostResponseDto> getOne(Long id){
        Post post = postRepository.findById(id).orElseThrow(
                ()-> new CustomException(ErrorCode.NotFoundPost)
        );
        PostResponseDto postResponseDto = post.toDto();
        return GlobalResDto.success(postResponseDto, null);
    }

    // 게시글 필터 조회
    public GlobalResDto<?> filterPost(String name) {
        List<Post> posts = postRepository.findAllByCategory_NameOrderByCreatedAtDesc(name);

        List<GetAllPostDto> getAllPostDtoList = getAllPost ( posts );

        return GlobalResDto.success ( getAllPostDtoList, null );
    }
}
