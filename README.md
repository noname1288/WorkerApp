# 🧰 HELPO – Ứng dụng dành cho Người cung cấp dịch vụ (Worker)

## 🏗️ Giới thiệu hệ thống

**HELPO** là ứng dụng trung gian giúp kết nối **người có nhu cầu dịch vụ (Khách hàng)** với **người cung cấp dịch vụ (Nhân viên)** thông qua nền tảng trực tuyến, hướng đến sự **minh bạch, tiện lợi và hiệu quả** trong quy trình làm việc.

Ứng dụng được phát triển với mục tiêu:

* 🔹 Tạo nền tảng uy tín giúp kết nối nhanh chóng giữa **Khách hàng** và **Người cung cấp dịch vụ**.
* 🔹 Tối ưu hoá quá trình **đăng tuyển – ứng tuyển – đánh giá dịch vụ**.
* 🔹 Cung cấp trải nghiệm **đơn giản, rõ ràng, dễ thao tác** cho cả ba đối tượng: **Quản trị viên**, **Người cung cấp dịch vụ**, và **Khách hàng**.

---

## 👥 Vai trò: **Người cung cấp dịch vụ (Worker)**

### 1. 🔑 Đăng ký tài khoản

* Người cung cấp dịch vụ cần **đăng ký tài khoản** với **Quản trị viên**.
* Sau khi hoàn tất **xác minh và phê duyệt**, tài khoản mới được kích hoạt.
* Khi đã trở thành **nhân viên chính thức**, người dùng có thể truy cập và sử dụng toàn bộ chức năng trong vai trò của mình.

### 2. 🔐 Đăng nhập

* Nhân viên nhập **Email** và **Mật khẩu** (được cấp hoặc đăng ký trước đó).
* Hệ thống xác thực và chuyển hướng đến **màn hình Trang chủ**.

### 3. 📋 Xem danh sách công việc

* Truy cập tab **Trang chủ** → hệ thống hiển thị danh sách các công việc đang tuyển theo **loại hình dịch vụ** và **khu vực**.
* Chọn một công việc để xem chi tiết: **mô tả, địa điểm, mức giá, thời gian, loại dịch vụ.**

### 4. 📨 Ứng tuyển công việc

* Nhấn **“Ứng tuyển”** tại trang chi tiết công việc.
* Hệ thống gửi yêu cầu đến **Khách hàng**, hiển thị thông báo *“Ứng tuyển thành công”*.
* Công việc được thêm vào danh sách **“Đang chờ duyệt”**.

### 5. ❌ Huỷ ứng tuyển

* Trước khi Khách hàng chấp thuận, nhân viên có thể huỷ ứng tuyển trong mục **“Ứng tuyển của tôi”**.
* Hệ thống cập nhật trạng thái và thông báo đến Khách hàng.

### 6. ✅ Nhận việc

* Khi Khách hàng **chấp thuận ứng tuyển**, hệ thống gửi thông báo *“Bạn đã được nhận”*.
* Nhân viên xác nhận **“Nhận việc”** → công việc chuyển sang trạng thái **“Đang thực hiện”**.

### 7. 🔧 Cập nhật tiến độ công việc

* Trong chi tiết công việc → chọn **“Bắt đầu làm việc”**.
* Sau khi hoàn tất → chọn **“Hoàn thành công việc”**.
* Hệ thống gửi thông báo đến Khách hàng để xác nhận hoàn tất.

### 8. ⭐ Xem đánh giá

* Sau khi Khách hàng đánh giá, nhân viên nhận thông báo và xem chi tiết trong mục **“Đánh giá”**.
* Hệ thống hiển thị **điểm trung bình và nhận xét** trên hồ sơ cá nhân.

### 9. 📅 Quản lý lịch làm việc

* Chọn tab **“Lịch làm việc”** để xem các công việc đã được duyệt theo **ngày/giờ**.
* Có thể **lọc theo trạng thái hoặc loại dịch vụ** để dễ dàng quản lý.

### 10.💬 Chat Realtime với khách hàng

* Chọn tab **“Tin nhắn”** để danh sách khách hàng từng trao đổi trong quá khứ.
* Chọn chi tiết một người và nhắn tin cho người đó.

---

## 🧩 Kiến trúc hệ thống

Ứng dụng được xây dựng theo mô hình **MVVM Architecture** kết hợp **Repository Pattern**, đảm bảo tách biệt rõ ràng giữa:

* **UI (Compose)** – giao diện hiện đại, dễ mở rộng.
* **ViewModel** – quản lý trạng thái, luồng dữ liệu bất đồng bộ.
* **Repository** – xử lý truy xuất dữ liệu từ **Firebase** và **API**.

### 📘 Biểu đồ hệ thống

* **Biểu đồ lớp (Class Diagram)**: mô tả chi tiết mối quan hệ giữa các thực thể như *User, Job, Order, Review, Notification…*
  
* **Biểu đồ Use Case**: thể hiện các chức năng chính của hệ thống đối với từng vai trò *(Client, Worker, Admin)*.

---

## ⚙️ Công nghệ sử dụng

| Thành phần      | Công nghệ                                        |
| --------------- | ------------------------------------------------ |
| Ngôn ngữ        | **Kotlin**                                       |
| UI Framework    | **Jetpack Compose**                              |
| Kiến trúc       | **MVVM + Repository Pattern**                    |
| CSDL & Realtime | **Firebase Firestore, Realtime Database (RTDB)** |
| Thông báo       | **Firebase Cloud Messaging (FCM)**               |
| Bản đồ          | **Google Map API**                               |
| Điều hướng      | **Navigation Component (Compose Navigation)**    |
| Luồng dữ liệu   | **Kotlin Flow, Coroutine**                       |

---

## 📱 Tính năng chính (Worker App)

* 🔍 **Tìm kiếm & lọc công việc** theo loại dịch vụ, địa điểm, thời gian.
* 📩 **Ứng tuyển – Huỷ ứng tuyển – Nhận việc – Cập nhật tiến độ.**
* 🔔 **Thông báo realtime (FCM)** khi có cập nhật đơn việc hoặc đánh giá.
* 📊 **Xem điểm đánh giá và phản hồi khách hàng.**
* 🗓️ **Quản lý lịch làm việc theo ngày/tuần.**
* 🧭 **Tích hợp bản đồ định vị và chỉ đường.**

---

## 🧠 Hướng phát triển tương lai

* Tích hợp **đề xuất việc làm thông minh** dựa trên kinh nghiệm và vị trí.
* Hỗ trợ **đa ngôn ngữ (Việt/Anh/Nhật)**.
* Thay đổi UI Mode (Light Mode/ Dark Mode)

---

Bạn có muốn mình giúp viết thêm **README (English version)** hoặc **chèn preview ảnh UML và Use Case** (markdown image links) để commit trực tiếp lên GitHub không?
