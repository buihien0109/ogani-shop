package vn.techmaster.ecommecerapp.utils.crawl;

import com.github.slugify.Slugify;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import vn.techmaster.ecommecerapp.entity.Blog;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.FileServer;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.repository.BlogRepository;
import vn.techmaster.ecommecerapp.repository.FileServerRepository;
import vn.techmaster.ecommecerapp.repository.UserRepository;
import vn.techmaster.ecommecerapp.service.FileServerService;

import java.io.ByteArrayOutputStream;
import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogCrawlerService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final FileServerRepository fileServerRepository;

    // Crawl và lưu vào database
    public void crawlAndSaveBlogPost(String url) {
        try {
            Document doc = Jsoup.connect(url).get();

            // get admin user
            User user = userRepository.findByEmail("admin@gmail.com")
                    .orElseThrow(() -> new ResouceNotFoundException("Not found user"));

            // Lấy thumbnail
            String imageUrl = getImageUrlFromMetaTag(doc);
//            byte[] imageBytes = downloadImage(imageUrl);
//            FileServer fileServer = new FileServer();
//            fileServer.setType("image/png");
//            fileServer.setData(imageBytes);
//            fileServer.setUser(user);
//            fileServerRepository.save(fileServer);

            // Lấy ra các thông tin cần thiết
            String title = doc.title();
            String description = doc.select("meta[name=description]").attr("content");
            Element contentElement = doc.selectFirst("#blog-content-center");

            // Loại bỏ tất cả các thuộc tính trong các thẻ HTML
            cleanHtmlAttributes(contentElement);
            String cleanedContent = contentElement.html();

            // Tạo slug
            Slugify slugify = Slugify.builder()
                    .customReplacement("đ", "d")
                    .customReplacement("Đ", "D")
                    .build();

            // Lưu vào database
            Blog blog = new Blog();
            blog.setTitle(title);
            blog.setSlug(slugify.slugify(title));
            blog.setContent(cleanedContent);
            blog.setDescription(description);
//            blog.setThumbnail("/api/v1/files/" + fileServer.getId());
            blog.setThumbnail(imageUrl);
            blog.setUser(user);
            blog.setStatus(true);
            blogRepository.save(blog);
        } catch (Exception e) {
            log.error("Error while crawling blog post", e);
        }
    }

    // Loại bỏ tất cả các thuộc tính trong các thẻ HTML
    public static void cleanHtmlAttributes(Element element) {
        element.removeAttr("class");
        element.removeAttr("id");
        element.removeAttr("style");
        element.removeAttr("width");
        element.removeAttr("height");
        element.removeAttr("data-src");
        // Thêm thêm các thuộc tính khác cần loại bỏ ở đây
        for (Element child : element.children()) {
            cleanHtmlAttributes(child);
        }
    }

    // Lấy ra URL của ảnh thumbnail từ thẻ meta
    public String getImageUrlFromMetaTag(Document document) {
        Element metaElement = document.select("meta[property=og:image]").first();
        if (metaElement != null) {
            String imageUrl = metaElement.attr("content");
            return imageUrl;
        }
        return null;
    }

    // Tải ảnh từ URL và chuyển thành mảng byte
    public byte[] downloadImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = connection.getInputStream()) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    return outputStream.toByteArray();
                }
            }
        } catch (IOException e) {
            log.error("Error while downloading image : {}", e.getMessage());
        }
        return null;
    }
}
