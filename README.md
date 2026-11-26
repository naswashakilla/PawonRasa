# 🍲 Pawon Rasa

**Sajian Lezat, Setiap Hari**

Pawon Rasa adalah aplikasi resep masakan untuk Android yang dibuat dengan **Jetpack Compose**. Data resep diambil dari [TheMealDB API](https://www.themealdb.com/api.php) (gratis, tanpa API key). Aplikasi mendukung dua bahasa (Indonesia dan Inggris), mode terang/gelap, serta tampilan yang tetap rapi dan bisa di-scroll saat layar diputar.

Proyek ini dibuat sebagai tugas individu: *Membuat Aplikasi Android yang Menerapkan Jetpack Compose dan API*.

---

## ✨ Fitur

- **Splash screen** dengan nama aplikasi, tagline, dan loading bar
- **Beranda**: sapaan, kolom pencarian, banner resep acak, filter kategori, dan daftar resep dalam bentuk card
- **Pencarian resep** berdasarkan kata kunci
- **Detail resep**: foto, kategori, asal masakan, bahan-bahan, dan langkah pembuatan bernomor
- **Favorit (♥)** dan **Koleksi (🔖)**: simpan resep, koleksi dapat difilter berdasarkan kategori, data tersimpan permanen
- **Pengaturan**: pilihan tema (Sistem / Terang / Gelap), pilihan bahasa, ringkasan data tersimpan, dan tombol hapus data
- **Multi bahasa**: antarmuka Indonesia dan Inggris, data API diterjemahkan otomatis ke bahasa Indonesia
- **Penanganan error**: pesan dan tombol "Coba Lagi" saat tidak ada internet, tanpa crash

---

## 📋 Pemenuhan Ketentuan Tugas

| Ketentuan | Penerapan |
|---|---|
| Jetpack Compose sebagai toolkit UI | Seluruh UI ditulis dengan Compose dan Material 3 |
| Dashboard semenarik mungkin | Beranda dengan banner gradasi, kategori, dan card resep |
| Menampilkan data dari API | Retrofit mengambil data dari TheMealDB |
| Multi bahasa | `values/strings.xml` (EN), `values-in/strings.xml` (ID), tombol ganti bahasa di Pengaturan |
| Card dalam RecyclerView | `LazyVerticalGrid` / `LazyColumn`, padanan RecyclerView di Compose |
| Mode Night dan Not Night | Skema warna terang dan gelap, bisa diganti dari Pengaturan |
| Rotasi layar tidak mengubah desain | `ViewModel`, grid adaptif, dan semua halaman bisa di-scroll |

---

## 🛠️ Teknologi

| Komponen | Fungsi |
|---|---|
| Kotlin | Bahasa pemrograman |
| Jetpack Compose + Material 3 | UI |
| Retrofit + Gson | Request API dan parsing JSON |
| Kotlin Coroutines + StateFlow | Proses asinkron dan state layar |
| ViewModel | Menyimpan state agar aman saat rotasi |
| Navigation Compose | Navigasi antar halaman dan bottom bar |
| Coil | Memuat gambar dari URL |
| SharedPreferences + Gson | Menyimpan Favorit, Koleksi, dan tema |
| AppCompat (per-app language) | Ganti bahasa dari dalam aplikasi |
| ML Kit Translate | Menerjemahkan data API ke bahasa Indonesia di perangkat |

---

## 🏗️ Arsitektur

Aplikasi memakai pola **MVVM** sederhana:

```
TheMealDB API → Retrofit → MealRepository → ViewModel → Compose UI
```

1. `MealApi` mendefinisikan endpoint.
2. `MealRepository` memanggil API dan membungkus hasilnya dengan `Result` agar error tidak membuat aplikasi crash.
3. `ViewModel` menyimpan hasil sebagai state `Loading`, `Success`, atau `Error`.
4. Compose UI menampilkan spinner, daftar card, atau tombol "Coba Lagi" sesuai state.

### Struktur proyek

```
app/src/main/java/com/shakilla/pawonrasa/
├── MainActivity.kt
├── data/
│   ├── Models.kt            # model respons API dan model UI
│   ├── MealApi.kt           # endpoint Retrofit
│   ├── RetrofitInstance.kt
│   ├── MealRepository.kt
│   ├── SavedMeal.kt         # model Favorit/Koleksi
│   └── Translator.kt        # terjemahan ML Kit
├── viewmodel/
│   ├── UiState.kt
│   ├── HomeViewModel.kt
│   ├── DetailViewModel.kt
│   ├── SavedViewModel.kt
│   └── SettingsViewModel.kt
└── ui/
    ├── AppNavigation.kt
    ├── SplashScreen.kt
    ├── HomeScreen.kt
    ├── DetailScreen.kt
    ├── SavedScreen.kt
    ├── SettingsScreen.kt
    ├── Components.kt
    ├── TranslateUi.kt
    └── theme/               # Color.kt, Type.kt, Theme.kt
```

---

## 🌐 API

Menggunakan [TheMealDB](https://www.themealdb.com/api.php) dengan key uji `1`.

| Endpoint | Kegunaan |
|---|---|
| `categories.php` | Daftar kategori |
| `filter.php?c={kategori}` | Resep per kategori |
| `search.php?s={kata}` | Pencarian resep |
| `lookup.php?i={id}` | Detail resep |
| `random.php` | Resep acak untuk banner |

---

## 🚀 Cara Menjalankan

**Prasyarat**
- Android Studio versi terbaru
- JDK 17 atau 21 (gunakan JDK bawaan Android Studio untuk Gradle)
- Perangkat atau emulator Android (minimum SDK 24) dengan koneksi internet

**Langkah**
1. Clone repository:
   ```bash
   git clone https://github.com/<username>/<nama-repo>.git
   ```
2. Buka folder proyek di Android Studio.
3. Tunggu proses **Gradle Sync** selesai.
4. Jalankan aplikasi (tombol ▶).

> Saat pertama kali memakai bahasa Indonesia, aplikasi mengunduh model terjemahan (±30 MB). Pastikan perangkat online. Setelah itu terjemahan juga berjalan offline.

---

## ⚠️ Catatan

- TheMealDB hanya menyediakan data berbahasa Inggris. Teks antarmuka memakai resource `strings.xml`, sedangkan nama resep, bahan, dan langkah diterjemahkan otomatis oleh ML Kit, sehingga hasilnya adalah terjemahan mesin dan bisa kurang sempurna.
- Pencarian resep sebaiknya memakai kata kunci bahasa Inggris (misalnya `chicken`).
- Membuka detail resep dari Favorit/Koleksi memerlukan internet.

---

## 📸 Tampilan

<!-- Tambahkan screenshot di folder screenshots/ lalu ganti nama file di bawah -->

| Beranda | Detail | Koleksi | Pengaturan |
|---|---|---|---|
| ![Beranda](screenshots/home.png) | ![Detail](screenshots/detail.png) | ![Koleksi](screenshots/collections.png) | ![Pengaturan](screenshots/settings.png) |

---

## 👩‍💻 Pembuat

**Shakilla**
Tugas Pemrograman Mobile, November – Desember 2026
