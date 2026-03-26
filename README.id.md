Terjemahan dari [README original](README.md). Bila terdapat perbedaan, prioritaskan original.

# Mindlessly Hiragana

Aplikasi Android untuk mempelajari huruf Hiragana dengan repetisi


## Index
* [Deskripsi](#deskripsi)
* [Panduan Memulai](#panduan-memulai)
* [Skill yang Diimplementasikan](#_skill_-yang-diimplementasikan)
* [Konsep yang Diimplementasikan](#konsep-yang-diimplementasikan)
* [Tentang Proyek dan Developer | Motivasi](#tentang-proyek-dan-developer--motivasi)



## Deskripsi
Biasanya, seseorang akan menghafalkan tabel Hiragana, menulis Hiragana berulang kali di kertas, dll
agar bisa membaca Hiragana. Aksi-aksi tersebut memerlukan banyak usaha kognitif, yang mana tidak 
dapat dilakukan sembari menonton video.

Aplikasi ini dibuat untuk seseorang yang ingin bisa membaca Hiragana dengan hanya perlu memilih 
tombol tanpa berfikir panjang, sehingga ia dapat belajar sambil menonton video.

Seorang penguji yang berusia 60+ tahun mampu membaca semua Hiragana yang diajarkan oleh aplikasi ini
dalam jangka waktu satu minggu tanpa pengawasan.

Aplikasi ini utamanya dibuat untuk mendemonstrasikan pemahaman terhadap _Official Layered_ 
_Architecture_ dan _Test Driven Development_.



### Screenshots
* [Interaksi Aplikasi di Perangkat Android](readme-media/app-interactions.mp4)
* [Home Screen](readme-media/home-screen.jpg)
* [Learn Screen](readme-media/learn-screen.jpg)
* [Quiz Screen](readme-media/quiz-screen.jpg)
* [Result Screen](readme-media/result-screen-all-correct.jpg)
* [Test Screen](readme-media/test-screen-locked.jpg)
* [Test Quiz Screen](readme-media/test-quiz-screen-keyboard-1.jpg)
* [Test Result Screen](readme-media/test-result-screen-all-correct.jpg)
* [Licenses Screen](readme-media/licenses-screen.jpg)



### Screen Flow
_Happy Path_ : Home Screen → Learn Screen → Quiz Screen → Result Screen (All Correct) → Test Screen
→ Test Quiz Screen → Test Result Screen (All Correct) → Learn Screen (Next Hiragana Category) → ...

_Sad Path_ 1 : Home Screen → Learn Screen → Quiz Screen → Result Screen (Some Incorrect)
→ Quiz Screen → ...

_Sad Path_ 2 : Home Screen → Test Screen → Test Quiz Screen → Test Result Screen (Some Incorrect)
→ Test Screen → ...

Home Screen → Licenses Screen



### Fitur
#### Home Screen
Pengguna disuguhkan layar ini saat ia meluncurkan aplikasi.

Terdapat 46 Hiragana yang diberikan kategori di layar ini. Semua kategori kecuali kategori pertama
dikunci secara _default_. Pengguna perlu menyelesaikan kategori secara berurutan. Kategori diurutkan
dari yang paling berbeda ke yang paling mirip. Pengguna akan dinavigasikan ke layar `Learn Screen`
saat mengklik kategori.

Pengguna dapat bernavigasi ke `Test Screen` dari layar ini.

Terdapat _drawer_ di sebelah kiri layar ini. Pengguna dapat memunculkan dialog untuk mereset 
kategori yang telah dicapai atau bernavigasi ke `Licenses Screen` dengan memilih _item drawer_ yang 
sesuai.

Pengguna direkomendasikan untuk mereset capaian kategorinya bila `Test Screen` dinilai terlalu 
sulit.

#### Learn Screen
Pengguna dapat mengatur `Learning Sets` di layar ini dari `1` hingga `10` dengan `5` sebagai nilai
_default_. Mengatur `5` sebagai `Learning Sets` di kategori `ひみかせ` berarti pengguna akan 
diberikan pertanyaan lima kali untuk tiap Hiragana, dengan total 20 pertanyaan.

Pengguna direkomendasikan untuk menjawab semua pertanyaan dengan benar menggunakan nilai _default_
setidaknya sekali.

#### Quiz Screen
Pengguna akan ditampilkan sebuah Hiragana beserta beberapa romaji, lalu pengguna dapat memilih 
salah satu romaji sebagai jawaban.

Pengguna diharapkan untuk tidak berfikir terlalu panjang saat memilih jawaban.

Bila pengguna memililh jawaban yang salah, maka tombol jawaban tersebut akan dinonaktifkan, 
sehingga pengguna pada akhirnya akan diarahkan ke jawaban yang benar.

#### Result Screen
Pengguna dapat melihat Hiragana yang telah dijawab serta jumlah salah menjawab.

Pengguna dapat mencoba kembali `Quiz Screen` atau bernavigasi ke `Test Screen` dengan memilih 
tombol yang sesuai.

Tombol untuk bernavigasi ke `Test Screen` hanya aktif bila semua pertanyaan dari `Quiz Screen`
dijawab dengan benar.

#### Test Screen
Pengguna disuguhkan daftar kategori hiragana yang akan diuji berdasarkan kategori yang tidak 
terkunci di `Home Screen`.

Pengguna perlu terlebih dahulu menjawab semua pertanyaan dengan benar di `Learn Screen` agar
tombol `Test All Learned` menjadi aktif.

#### Test Quiz Screen
_User flow_ di layar ini mirip dengan _user flow_ di `Quiz Screen`, namun romaji dari semua 
hiragana dapat dipilih sebagai jawaban.

Setiap hiragana yang menjadi pertanyaan hanya ditampilkan sekali di layar ini.

#### Test Result Screen
Bila semua pertanyaan dijawab dengan benar di `Test Quiz Screen`, maka kategori hiragana baru di 
`Home Screen` serta tombol `Continue Learning` akan menjadi aktif.

#### Licenses Screen
Layar ini menampilkan daftar semua _open source libraries_ yang digunakan oleh aplikasi ini.



## Panduan Memulai
### Instalasi
* Unduh APK build, lalu install ke perangkat Android.



### Histori Versi
* 1.0 : _Minimum viable product_ dengan fitur dasar.



### Detail Branch Git
* `master` adalah branch utama yang diperuntukkan untuk di-_build_
* `readme`, `gplay-licenses-activity`, `firebase-analytics-implementation`, `refactor-applicationid`,
`refactor-robolectric`, `tdd-refactor-to-bdd`, `main-tdd`, `record-interaction` adalah _branch_ 
untuk fitur dan _refactor_ yang di-merge ke `master`.
* `Branch` selain yang diatas masih ada hanya untuk keperluan historis.



### Perangkat yang Diuji
* Pixel 9 Pro Fold (emulator)
* Redmi Note 12 (perangkat fisik)



## _Skill_ yang diimplementasikan
* UI: Jetpack Compose | Material Design 3
* Dependency Injection: Hilt
* Testing: Robolectric | Gherkin
* Asynchronous: Kotlin Flow
* Navigation: Jetpack Navigation 2
* Source Control: Git | GitHub
* Libraries: Firebase Analytics | Google Play Services



## Konsep yang diimplementasikan
* Unidirectional Data Flow
* Repository Pattern
* Reactive Programming
* Dependency Injection
* State Hoisting
* Test Coverage
* Code Readability
* Magic Value Elimination



## Tentang Proyek dan Developer | Motivasi
Saya membuat proyek ini untuk mendemonstrasikan pemahaman saya terhadap _Official Layered_
_Architecture_ sebagaimana yang tertera pada dokumentasi Android. Pada awalnya, saya membuat proyek 
ini secepat mungkin untuk menghasilkan MVP yang berskala kecil namun berfungsi tanpa konsiderasi 
apapun terhadap arsitektur. Kemudian saya mengimplementasikan arsitektur yang direkomendasikan 
pada dokumentasi Android. Pada titik ini masih belum ada pengujian yang dilakukan.

Saya belajar bahwa melakukan pengembangan aplikasi tanpa kode pengujian berarti saya perlu membuka 
emulator secara manual dan menguji setiap fitur, serta berharap fitur sebelumnya masih berjalan
dengan semestinya saat saya mengimplementasikan fitur baru, yang mana fitur sebelumnya memang benar 
menjadi tidak berjalan dengan semestinya.

Lalu saya mengembalikan semua perubahan yang telah saya buat dalam proyek ini ke commit pertama. 
Setelah menyadari betapa pentingnya pengujian, saya tertarik dengan konsep yang ditawarkan oleh
_Test Driven Development_ (TDD). Kemudian saya mengimplementasikan ulang proyek dari awal tanpa 
mereferensi kode apa pun yang telah dikembalikan. Kedua implementasi tersebut hanya merujuk pada 
proyek Figma yang telah saya buat sebelumnya. Saya mengimplementasikan _instrumented test_ dengan 
Compose Testing API. Setelah itu saya mempertanyakan bagaimana caranya untuk mengurangi jumlah 
duplikasi dalam kode pengujian, dan saya menemukan konsep yang sangat menarik, yaitu Robot Pattern 
oleh Jake Wharton.

Saya mengalami sendiri betapa mudahnya bila setiap perubahan yang dibuat pada sebuah proyek pasti 
tidak akan merusak fitur apa pun yang telah Anda buat sebelumnya dengan mengikuti TDD. Namun saya 
juga merasakan betapa lambatnya membuat pengujian sebelum mengimplementasikan fitur apapun, 
walaupun hal ini mungkin hanya terjadi terutama karena saya masih mempelajari Compose Testing API. 
Meskipun demikian, berkat TDD, setiap fitur telah teruji.

Setelah banyak membaca tentang desain pengujian _software_, saya menyadari bahwa Robot Pattern 
mungkin cocok untuk proyek yang jauh lebih besar dan terlalu berlebihan untuk proyek sesederhana 
proyek saya. Kemudian saya tertarik dengan konsep Behavior Driven Development (BDD) dan Gherkin. 
Saat saya mengimplementasikan pengujian, sangat sulit untuk mengubah pengujian manapun karena kode 
pengujian tersebut tersebar di mana-mana. Oleh karena itu, saya memastikan bahwa setiap pengujian 
hanya terdapat dalam satu _function_ dengan perilaku pengujiannya yang dijelaskan sebagai komentar 
mengikuti format Gherkin. Pengujian juga diwajibkan untuk tidak memiliki _detail_ implementasi di 
dalamnya, dengan hanya menjelaskan dan menguji perilaku dari perspektif pengguna.

Saat melakukan pengembangan aplikasi, datang masa dimana saya perlu mengakses dan mengembangkan 
proyek ini dari laptop saya. Saat menjalankan _integration test_, prosesnya memakan waktu terlalu 
lama karena spesifikasi laptop saya tidak sebaik PC saya. Setelah melakukan beberapa riset, saya 
menemukan bahwa Google sendiri merekomendasikan Robolectric, _library_ yang sangat cocok untuk 
situasi saya saat itu. Migrasi kode pengujian dibuat sangat mudah, dan _fidelity trade-off_ 
tidak menjadi masalah bagi proyek ini, sehingga saya memutuskan untuk bermigrasi ke Robolectric. 
Dengan bermigrasi, kode tes berjalan lebih cepat di laptop saya karena saya tidak perlu menggunakan 
_emulator_ apa pun.

Pada titik ini, saya sebenarnya menganggap proyek ini telah selesai, karena ketika saya meminta ibu 
saya untuk mencoba menggunakan aplikasi ini, beliau, yang berusia 60+ tahun, mampu membaca Hiragana 
apa pun yang diajarkan dalam aplikasi ini dalam waktu kurang dari seminggu. Namun, ketika saya 
mencari lowongan pekerjaan junior, banyak di antaranya yang meminta pengalaman dalam integrasi 
Firebase dan Google Play. Untungnya, tim Google luar biasa dalam membuat integrasi yanng semudah 
mungkin. Saya mengimplementasikan _menu_ untuk melihat _open-source library_ yang digunakan oleh 
aplikasi ini dengan Google Play OSS Licenses Library serta mengimplementasikan pelacakan sederhana 
capaian pembelajaran pengguna dengan _custom event_ dari Firebase Analytics.

Implementasi final proyek ini adalah _release build_ versi 1.0.

(Saya juga ingin mempublikasikan aplikasi ini ke Google Play Store, namun menemukan _private tester_ 
untuk aplikasi ini hampir mustahil bagi saya saat ini, belum termasuk biaya untuk membuat 
Play Console Developer Account.)