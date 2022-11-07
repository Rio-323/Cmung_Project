package com.sparta.cmung_project.service;

import com.sparta.cmung_project.model.Image;
import com.sparta.cmung_project.model.Post;
import com.sparta.cmung_project.repository.ImageRepository;
import com.sparta.cmung_project.repository.PostRepository;
import com.sparta.cmung_project.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final ImageRepository imgRepository;

    private final S3Service s3Service;

    @Transactional
    public GlobalResDto<PostResponseDto> createPost(String postRequestDto, List<MultipartFile> file, Member member){
        List<Image> imgs = new ArrayList<>();
        Post post = new Post(postRequestDto,member);
        for (MultipartFile multipartFile : file) {
            Img img = imgRepository.save(new Img(s3Service.uploadFile(multipartFile),post));
            imgs.add(img);
        }
        post.setImgs(imgs);
        postRepository.save(post);
        PostResponseDto postResponseDto = new PostResponseDto(post);

        List<String> imgList = new ArrayList<>();
        for (Img img:imgs) {
            imgList.add(img.getImage());
        }

        postResponseDto.setImgs(imgList);

        return GlobalResDto.success(postResponseDto,"게시글 작성 완료되었습니다.");
    }

    @Transactional(readOnly = true)
    public GlobalResDto<?> allPost(){
        List<Post> posts = postRepository.findAll();
        List<AllPostResponseDto> postResponseDtos = new ArrayList<>();

        for(Post p : posts){
            List<String> imgList = new ArrayList<>();
            for(Img i : p.getImgs()){
                imgList.add(i.getImage());
            }
            postResponseDtos.add(new AllPostResponseDto(p, imgList));
        }

        return GlobalResDto.success(postResponseDtos,"조회성공");
    }

    @Transactional(readOnly = true)
    public GlobalResDto<OnePostResponseDto> onePost(Long postId, Long imageId,Member currentMember){
        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new CustomException("글 조회", ErrorCode.NotFound));
        List<Img> imgList = post.getImgs();
        List<String> imgs = new ArrayList<>();
        for(Img a : imgList){
            imgs.add(a.getImage());
        }
//        Optional<Img> img = imgRepository.findById(imageId);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        Boolean amILike = postLikeRepository.existsByMemberAndPost(currentMember, post);
        for (Comment comment : post.getCommentList()){
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }
        OnePostResponseDto onePostResponseDto = new OnePostResponseDto(post,imgs, commentResponseDtoList, amILike);
        return GlobalResDto.success(onePostResponseDto,"조회 성공");
    }


    @Transactional
    public GlobalResDto<PostResponseDto> delPost(Long postId,Member member){
        Post post = postRepository.findPostByPostIdAndMember(postId, member);
        if (post == null) return GlobalResDto.fail("삭제 권한이 없습니다.");
        postRepository.deleteById(post.getPostId());
        return GlobalResDto.success(null,"삭제 되었습니다.");
    }

    @Transactional
    public GlobalResDto<PostResponseDto> modifyPost(Long postId,MultipartFile file, String contents, Member member){
        Post post = postRepository.findPostByPostIdAndMember(postId,member);
        if (post==null) return GlobalResDto.fail("수정 권한이 없습니다.");
        post.setContents(contents);
        PostResponseDto postResponseDto = new PostResponseDto(postRepository.save(post));
        return GlobalResDto.success(postResponseDto,"수정이 완료 되었습니다.");

    }
}
