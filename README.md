Pertemuan 6 - State, Event, dan Form Validation (Tugas Individu)
Form pendaftaran akun mahasiswa satu layar, dibuat dengan Jetpack Compose dan Material 3. Tidak memakai Navigation, database, atau API, dan tidak mengirim password ke log atau server. Ini simulasi validasi di sisi UI saja.
Yang diubah dari contoh praktikum
Contoh praktikum hanya diganti nama saja tidak cukup, jadi versi ini diubah di beberapa tempat:
Palet warna: hijau zamrud gelap 0xFF0E7A5F sebagai warna utama, amber gelap 0xFFB45309 sebagai warna kedua, dan latar krem 0xFFF7F5EF. Contoh aslinya memakai ungu 0xFF5B4BA8 dan teal 0xFF2C7A7B.
Label: judul atas menjadi App KampusRDN, judul besar menjadi Daftar Akun Mahasiswa, label email menjadi Email kampus, tombol menjadi Buat Akun, dan pesan sukses menjadi Pendaftaran berhasil dengan menyebut nama dan NIM.
Field dan aturan tambahan: NIM Mahasiswa. Aturannya NIM harus tepat 12 angka, bukan hanya terisi, dan kolomnya hanya menerima angka saja, huruf atau simbol yang diketik akan langsung dibuang sebelum masuk ke state. Kalau NIM kosong, kurang dari 12 angka, atau lebih dari 12 angka, formulir dinyatakan tidak valid.
Komponen hoisting: field NIM dibuat sebagai komponen InputNim yang terpisah. Komponen ini tidak menyimpan state sendiri, ia hanya menerima nilai lewat parameter dan melaporkan perubahan lewat callback. Pemilik state tetap RegistrationScreen.
Usability tambahan: field konfirmasi kata sandi juga diberi tombol tampilkan atau sembunyikan, sama seperti field kata sandi.
Bukti syarat tugas individu
Syaratnya bukan hanya mengganti nama aplikasi, dan file ini memenuhinya di empat tempat yang bisa dicek langsung di kode:
Tema aplikasi: MaterialTheme dengan lightColorScheme sendiri, bukan theme contoh. Warna utama, warna kedua, dan latar diubah, lihat di MainActivity bagian setContent.
Label: judul atas, judul besar, judul kartu Data Mahasiswa, label Email kampus, teks tombol, dan teks popup sukses semuanya diubah dari contoh, bukan hanya nama aplikasi di judul atas.
Palet warna: contoh memakai ungu 0xFF5B4BA8, teal 0xFF2C7A7B, dan latar 0xFFF6F5FA. Versi ini memakai hijau zamrud 0xFF0E7A5F, amber gelap 0xFFB45309, dan latar krem 0xFFF7F5EF.
Field dan aturan tambahan: field NIM Mahasiswa dengan komponen InputNim sendiri, aturan nimValid yaitu tepat 12 angka dan hanya angka, filter digit di onValueChange, dan aturan ini ikut menentukan formValid. Tambahan lain: popup sukses AlertDialog di tengah layar dan tombol tampilkan atau sembunyikan untuk konfirmasi kata sandi.
Fitur
Input nama lengkap, NIM, email kampus, kata sandi, dan konfirmasi kata sandi.
Checkbox persetujuan ketentuan penggunaan.
Validasi per kolom yang baru muncul setelah tombol Buat Akun ditekan.
Pesan error di bawah kolom yang bermasalah, dengan border kolom berwarna merah.
Pesan sukses hanya muncul kalau semua aturan terpenuhi, dan tampil sebagai popup AlertDialog di tengah layar, bukan lagi teks di bawah tombol. Popup ditutup dengan tombol OK atau dengan menekan area di luar popup.
Layar bisa di-scroll supaya formulir tidak terpotong di layar kecil.
Tombol tampilkan atau sembunyikan kata sandi dan konfirmasi kata sandi.
Aturan validasi
Nama lengkap: tidak boleh kosong.
NIM: harus tepat 12 angka.
Email: harus sesuai format alamat email yang valid, dicek dengan Patterns.EMAIL_ADDRESS.
Kata sandi: minimal 8 karakter.
Konfirmasi kata sandi: harus sama dengan kata sandi.
Persetujuan: checkbox wajib dicentang.
Pesan sukses akan hilang atau diperbarui kalau pengguna mengubah input setelah submit, karena setiap onValueChange mengatur ulang status berhasil menjadi false.
State yang dipakai
rememberSaveable untuk nama, NIM, email, persetujuan, dan toggle tampilkan kata sandi, supaya input tidak langsung hilang saat konfigurasi berubah, misalnya rotasi layar.
remember untuk kata sandi, konfirmasi kata sandi, status sudah submit, dan status berhasil, karena kata sandi bersifat sementara dan tidak perlu dipulihkan dari saved state.
Setiap perubahan input adalah event. Event itu mengubah state, lalu Compose melakukan recomposition pada bagian UI yang membaca state tersebut.
Cara menjalankan
Buka project Pertemuan_6 di Android Studio.
Ganti isi file app/src/main/java/com/example/pertemuan_6/MainActivity.kt dengan file MainActivity.kt di repo ini.
Pastikan package di baris pertama tetap com.example.pertemuan_6, sesuai nama package project loe.
Pastikan dependency Material 3, Compose BOM, Activity Compose, dan material-icons-extended sudah ada di build.gradle.kts (Module: app), lalu klik Sync Now dan tunggu sampai selesai.
Jalankan aplikasi di emulator atau HP, dengan tombol Run.
Versi ini memakai MaterialTheme langsung di MainActivity dan di preview, jadi tidak perlu import theme dari project lain. Kalau masih ada import StateEventFormValidationTheme, hapus baris itu.
Skenario pengujian
Uji minimal lima kondisi ini, jangan hanya kondisi benar:
Kosong semua, lalu tekan Buat Akun. Harusnya muncul error nama, NIM, email, kata sandi, konfirmasi, dan persetujuan, tanpa pesan sukses.
Email tidak valid, misalnya raden@, lalu tekan Buat Akun. Harusnya muncul pesan email tidak valid.
NIM tidak 12 angka, misalnya 24108, lalu tekan Buat Akun. Harusnya muncul pesan NIM harus 12 angka.
Kata sandi 7 karakter, atau konfirmasi berbeda dengan kata sandi, lalu tekan Buat Akun. Harusnya muncul pesan minimal 8 karakter atau konfirmasi harus sama.
Semua valid, misalnya nama terisi, NIM 12 angka, email valid, kata sandi minimal 8 karakter, konfirmasi sama, dan checkbox dicentang, lalu tekan Buat Akun. Harusnya muncul pesan Pendaftaran berhasil.
Screenshot yang dikumpulkan
Screenshot harus diambil dari aplikasi yang benar-benar dijalankan, bukan dari preview atau mockup:
Screenshot aplikasi saat running, formulir tampil lengkap.
Screenshot kondisi validasi gagal, misalnya setelah menekan Buat Akun dengan kolom kosong atau email tidak valid.
Screenshot kondisi validasi berhasil, setelah mengisi semua kolom dengan benar dan menekan Buat Akun, dengan popup Pendaftaran berhasil tampil di tengah layar.
