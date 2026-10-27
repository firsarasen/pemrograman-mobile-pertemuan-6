package com.example.pertemuan_6

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Activity utama aplikasi yang menjadi titik masuk (entry point).
 * Mengatur tema dasar dan menampilkan layar pendaftaran [RegistrationScreen].
 * Versi tugas individu: palet diubah ke hijau zamrud dan amber, label diubah,
 * dan ditambah field NIM Mahasiswa dengan aturan validasinya sendiri.
 * Tema di sini memakai MaterialTheme langsung supaya tidak bergantung pada
 * nama theme project lain.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF0E7A5F),
                    onPrimary = Color.White,
                    secondary = Color(0xFFB45309),
                    background = Color(0xFFF7F5EF),
                    surface = Color.White
                )
            ) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    RegistrationScreen()
                }
            }
        }
    }
}

/**
 * Screen-level state owner untuk contoh state hoisting dasar.
 * Memperagakan penerapan State, Event, Anatomi Field (OutlinedTextField), dan Form Validation di Jetpack Compose.
 */
@Composable
fun RegistrationScreen() {
    // =========================================================================
    // 1. STATE MANAGEMENT
    // State adalah data yang menentukan apa yang ditampilkan oleh UI.
    // Ketika state berubah, Jetpack Compose akan melakukan Rekomposisi (Recomposition).
    // =========================================================================

    // State input formulir
    // - rememberSaveable: Menjaga state tetap bertahan saat terjadi perubahan konfigurasi (misal: rotasi layar).
    var nama by rememberSaveable { mutableStateOf("") }
    var nim by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }

    // - remember: Menjaga state selama rekomposisi UI, namun nilai akan di-reset jika Activity dihancurkan.
    //   Password bersifat sementara (transient) dan tidak disimpan dalam saved UI state demi alasan keamanan.
    var password by remember { mutableStateOf("") }
    var konfirmasiPassword by remember { mutableStateOf("") }
    var setuju by rememberSaveable { mutableStateOf(false) }
    var passwordTerlihat by rememberSaveable { mutableStateOf(false) }
    var konfirmasiTerlihat by rememberSaveable { mutableStateOf(false) }

    // State status pengiriman dan hasil validasi
    var sudahSubmit by remember { mutableStateOf(false) }
    var berhasil by remember { mutableStateOf(false) }

    // =========================================================================
    // 2. FORM VALIDATION (VALIDASI FORMULIR)
    // Evaluasi validasi dihitung secara reaktif/deklaratif setiap kali terjadi perubahan state input.
    // =========================================================================
    // Evaluasi pesan error untuk setiap field (hanya ditampilkan setelah tombol submit ditekan)
    val errorNama = sudahSubmit && nama.trim().isEmpty()
    val nimBersih = nim.trim()
    val nimValid = nimBersih.length == 12 && nimBersih.all { it.isDigit() }
    val errorNim = sudahSubmit && !nimValid
    val errorEmail = sudahSubmit && !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    val errorPassword = sudahSubmit && password.length < 8
    val errorKonfirmasi = sudahSubmit && konfirmasiPassword != password
    val errorPersetujuan = sudahSubmit && !setuju

    // Seluruh formulir dinyatakan valid jika semua kriteria terpenuhi:
    // 1. Nama tidak kosong.
    // 2. NIM tepat 12 angka.
    // 3. Email sesuai format alamat email yang valid.
    // 4. Password minimal 8 karakter.
    // 5. Konfirmasi password sama dengan password.
    // 6. Checkbox persetujuan telah dicentang.
    val formValid = nama.trim().isNotEmpty() &&
            nimValid &&
            Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() &&
            password.length >= 8 && konfirmasiPassword == password && setuju

    // Layout utama layar dengan dukungan scroll vertikal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header aplikasi
        Text("App KampusRDN", fontSize = 16.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Text("Daftar Akun Mahasiswa", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF20213A))
        Text("Isi data di bawah ini, akun dibuat setelah semua kolom lolos validasi.", color = Color(0xFF666779), fontSize = 14.sp)

        // Kartu kontainer formulir
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Data Mahasiswa", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF20213A))

                // =========================================================================
                // 3. ANATOMI FIELD (OutlinedTextField) & EVENT HANDLING
                // Anatomi Field di Jetpack Compose terdiri dari:
                // - value: Nilai teks saat ini dari State.
                // - onValueChange (EVENT): Callback yang dipanggil saat pengguna mengetik/mengubah nilai.
                // - label: Label melayang di atas field.
                // - leadingIcon: Ikon di sisi kiri field.
                // - trailingIcon: Ikon aksi/pilihan di sisi kanan field.
                // - visualTransformation: Mengubah penampakan teks (misal: menyembunyikan password).
                // - isError: Indikator boolean untuk menandai status error.
                // - supportingText: Teks petunjuk atau pesan kesalahan di bawah field.
                // - keyboardOptions: Mengatur tipe input keyboard virtual (misal: Email, Password).
                // =========================================================================

                // Anatomi Field 1: Input Nama Lengkap
                OutlinedTextField(
                    value = nama, // [Anatomi Field: Value] State penampung teks nama
                    onValueChange = { // [EVENT] Dipanggil setiap ada perubahan teks dari input pengguna
                        nama = it
                        berhasil = false // [EVENT Effect] Reset status berhasil saat pengguna mengubah input
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nama lengkap") }, // [Anatomi Field: Label]
                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) }, // [Anatomi Field: Leading Icon]
                    singleLine = true,
                    isError = errorNama, // [Anatomi Field & Validasi: Status Error]
                    supportingText = { if (errorNama) Text("Nama wajib diisi") } // [Anatomi Field & Validasi: Supporting Text]
                )

                // Field tambahan tugas individu: Input NIM, dibuat lewat komponen hoisting di bawah.
                // RegistrationScreen tetap pemilik state nim, InputNim hanya menampilkan dan melaporkan event.
                InputNim(
                    nim = nim,
                    onNimChange = {
                        nim = it
                        berhasil = false
                    },
                    isError = errorNim,
                    showError = errorNim
                )

                // Anatomi Field 2: Input Email
                OutlinedTextField(
                    value = email, // [Anatomi Field: Value]
                    onValueChange = { // [EVENT]
                        email = it
                        berhasil = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email kampus") }, // [Anatomi Field: Label]
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) }, // [Anatomi Field: Leading Icon]
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), // [Anatomi Field: Keyboard Options]
                    isError = errorEmail, // [Anatomi Field & Validasi: Status Error]
                    supportingText = { if (errorEmail) Text("Masukkan alamat email yang valid") } // [Anatomi Field & Validasi: Supporting Text]
                )

                // Anatomi Field 3: Input Kata Sandi
                OutlinedTextField(
                    value = password, // [Anatomi Field: Value]
                    onValueChange = { // [EVENT]
                        password = it
                        berhasil = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Kata sandi") }, // [Anatomi Field: Label]
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) }, // [Anatomi Field: Leading Icon]
                    trailingIcon = { // [Anatomi Field: Trailing Icon] Tombol toggle visibilitas kata sandi
                        IconButton(onClick = { // [EVENT] Toggle nilai boolean passwordTerlihat saat ikon diklik
                            passwordTerlihat = !passwordTerlihat
                        }) {
                            Icon(
                                imageVector = if (passwordTerlihat) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (passwordTerlihat) "Sembunyikan kata sandi" else "Tampilkan kata sandi"
                            )
                        }
                    },
                    visualTransformation = if (passwordTerlihat) VisualTransformation.None else PasswordVisualTransformation(), // [Anatomi Field: Visual Transformation]
                    singleLine = true,
                    isError = errorPassword, // [Anatomi Field & Validasi: Status Error]
                    supportingText = { if (errorPassword) Text("Kata sandi minimal 8 karakter") } // [Anatomi Field & Validasi: Supporting Text]
                )

                // Anatomi Field 4: Input Konfirmasi Kata Sandi, ditambah toggle tampilkan seperti field password.
                OutlinedTextField(
                    value = konfirmasiPassword, // [Anatomi Field: Value]
                    onValueChange = { // [EVENT]
                        konfirmasiPassword = it
                        berhasil = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Konfirmasi kata sandi") }, // [Anatomi Field: Label]
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) }, // [Anatomi Field: Leading Icon]
                    trailingIcon = {
                        IconButton(onClick = {
                            konfirmasiTerlihat = !konfirmasiTerlihat
                        }) {
                            Icon(
                                imageVector = if (konfirmasiTerlihat) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (konfirmasiTerlihat) "Sembunyikan konfirmasi" else "Tampilkan konfirmasi"
                            )
                        }
                    },
                    visualTransformation = if (konfirmasiTerlihat) VisualTransformation.None else PasswordVisualTransformation(), // [Anatomi Field: Visual Transformation]
                    singleLine = true,
                    isError = errorKonfirmasi, // [Anatomi Field & Validasi: Status Error]
                    supportingText = { if (errorKonfirmasi) Text("Konfirmasi harus sama dengan kata sandi") } // [Anatomi Field & Validasi: Supporting Text]
                )

                // Checkbox Persetujuan Ketentuan (Event & Validasi)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = setuju, // [State] Menunjukkan status centang
                        onCheckedChange = { // [EVENT] Dipanggil saat Checkbox diklik
                            setuju = it
                            berhasil = false
                        }
                    )
                    Text("Saya menyetujui ketentuan penggunaan", fontSize = 13.sp)
                }
                if (errorPersetujuan) { // [Form Validation: Pesan error persetujuan]
                    Text("Persetujuan wajib dicentang", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                // =========================================================================
                // 4. EVENT SUBMIT FORMULIR
                // Mengatur alur saat pengguna menekan tombol pendaftaran.
                // =========================================================================
                Button(
                    onClick = { // [EVENT] Dipanggil saat tombol "Buat Akun" diklik oleh pengguna
                        sudahSubmit = true // Memicu pembaruan state bahwa formulir telah dikirim
                        berhasil = formValid // Menyimpan hasil validasi formulir
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Buat Akun", modifier = Modifier.padding(vertical = 3.dp))
                }

            }
        }

        // Popup sukses di tengah layar, muncul hanya kalau semua validasi lolos.
        // AlertDialog di Material 3 tampil di tengah layar secara default.
        if (berhasil) {
            AlertDialog(
                onDismissRequest = { berhasil = false },
                title = { Text("Pendaftaran berhasil") },
                text = { Text("Halo, ${nama.trim()}! Akun dengan NIM $nimBersih siap dipakai.") },
                confirmButton = {
                    TextButton(onClick = { berhasil = false }) {
                        Text("OK")
                    }
                }
            )
        }
        Text("Latihan: isi semua kolom, NIM 12 angka, gunakan email valid, kata sandi minimal 8 karakter, lalu centang persetujuan.", fontSize = 12.sp, color = Color(0xFF666779))
    }
}

/**
 * Contoh state hoisting untuk field NIM.
 * Komponen ini tidak menyimpan salinan state sendiri, ia hanya menerima nilai
 * lewat parameter nim dan melaporkan perubahan lewat onNimChange.
 * Pemilik state tetap RegistrationScreen, jadi aturan validasi dan pesan error
 * tetap dihitung di satu tempat.
 */
@Composable
fun InputNim(
    nim: String,
    onNimChange: (String) -> Unit,
    isError: Boolean,
    showError: Boolean
) {
    OutlinedTextField(
        value = nim,
        // Hanya angka yang diteruskan ke state, huruf dan simbol lain dibuang saat diketik.
        onValueChange = { onNimChange(it.filter { it.isDigit() }) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("NIM Mahasiswa") },
        leadingIcon = { Icon(Icons.Filled.Badge, contentDescription = null) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = isError,
        supportingText = { if (showError) Text("NIM harus 12 angka") }
    )
}

/**
 * Fungsi Preview untuk menampilkan tampilan [RegistrationScreen] pada Android Studio Interactive Preview.
 */
@Preview(showBackground = true)
@Composable
    fun RegistrationScreenPreview() {
        MaterialTheme {
            RegistrationScreen()
        }
    }
