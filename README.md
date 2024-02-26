## Trang web bán hàng thực phẩm sạch Organic

**Link demo**: http://103.237.147.34:8887/

**Trang đăng nhập**: http://103.237.147.34:8887/dang-nhap

```
Role : ROLE_ADMIN
Email : admin@gmail.com
Password : 123

Role : ROLE_USER
Email : toan@gmail.com
Password : 123
```

### Triển khai ứng dụng

Sau khi clone source về máy, có thể triển khai ứng dụng theo 2 cách:

#### 1. Chạy ứng dụng với maven

```bash
mvn spring-boot:run
```

#### 2. Triển khai ứng dụng với Docker Compose

```
docker-compose up -d
```

### Công nghệ sử dụng

- Spring Boot
- Spring Security
- Spring Data JPA
- Thymeleaf
- MySQL
- Docker
- Docker Compose
- ...

### Các chức năng chính

#### 1. Người dùng

- Tìm kiếm thông tin sản phẩm theo danh mục, tên, ...
- Xem thông tin chi tiết sản phẩm
- Thêm sản phẩm vào giỏ hàng, danh sách yêu thích
- Xem thông tin các bài viết
- Đăng ký tài khoản, đăng nhập, đăng xuất, quên mật khẩu
- Quản lý thông tin cá nhân: acount, địa chỉ, đơn hàng, ....
- Đánh giá sản phẩm
- Đặt hàng, thanh toán,...

#### 2. Quản trị viên

- Xem các thông số thống kê tổng quan
- Quản lý bài viết, danh mục bài viết
- Quản lý sản phẩm, danh mục sản phẩm
- Xem báo cáo thu chi, xuất file excel
- Quản lý nhà cung cấp, đơn hàng của nhà cung cấp
- Quản lý đơn hàng của khách hàng
- Quản lý người dùng
- Danh sách chiến dịch giảm giá, coupon giảm giá
- Cấu hình hệ thống: banner
- ...