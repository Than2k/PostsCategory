package com.example.postscategory.service.impl;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.postscategory.model.Posts;
import com.example.postscategory.repository.PostsRepository;
import com.example.postscategory.service.IPostsService;

@Service
public class PostsServiceImpl  implements IPostsService{
	private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
	@Autowired
	private PostsRepository postsRepository;
	
	@Override
	public Iterable<Posts> findAll() {
		return postsRepository.findAll();
	}

	@Override
	public void save(Posts data) {
		postsRepository.save(data);
	}

	@Override
	public void delete(int id) {
		postsRepository.deleteById(id);;
	}

	@Override
	public Optional<Posts> findOne(int id) {
		return postsRepository.findById(id);
	}

	@Override
	public Posts getPostdByID(int id) {
		return postsRepository.getPostdByID(id);
	}
	@Override
	public List<Posts> listPosts(int category_id,int page, int pageSize,String searchValues) {
		if(searchValues != "")
			searchValues = "%" +searchValues +"%";
		return postsRepository.listPosts(category_id,((page-1)*pageSize), pageSize, searchValues);
	}
	@Override
	public int count(int category_id, String searchValues) {
		if(searchValues != "")
			searchValues = "%" +searchValues +"%";
		return postsRepository.count(category_id, searchValues);
	}

	@Override
	public String upLoadImage(MultipartFile pathFile) throws Exception{
		String image = null;
		// if ảnh được chọn mới xử lý thêm vào server
		if (!pathFile.isEmpty()) {

			Path staticPath = Paths.get("src/main/resources/static/images");// đường dẫn lưu ảnh
			Path imagePath = Paths.get("");// đường dẫn
			// kiểm tra xem ảnh đó tồn tại không
			if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {																						 

				Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
			}
			Path file = CURRENT_FOLDER.resolve(staticPath).resolve(imagePath)
					.resolve(pathFile.getOriginalFilename());
			image = imagePath.resolve(pathFile.getOriginalFilename()).toString();// lấy link tên file ảnh
			try (OutputStream os = Files.newOutputStream(file)) {
				os.write(pathFile.getBytes());
			}
		}
		return image;
	}
	
}
