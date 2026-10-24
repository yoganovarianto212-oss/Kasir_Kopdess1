# Kasir Koperasi Desa — Java 21, Maven, MySQL, NetBeans 31

## Teknologi
Java Swing, Maven, FlatLaf Dark, MySQL/MariaDB XAMPP. Target Java: 21.

## Form UI terpisah
- `FormLogin` — halaman pembuka.
- `FormDashboard` — menu utama.
- `FormBarang` — CRUD dan pencarian barang.
- `FormKasir` — keranjang, nama pembeli, checkout.
- `FormRiwayat` — pencarian transaksi dan detail item historis.

Setiap form memiliki file `.java` dan sidecar `.form`. Layout dan event handler ditulis dalam Java agar mudah dibaca, dengan file `.form` disertakan sebagai metadata form NetBeans. Bila GUI Builder tidak dapat mengurai metadata minimal pada versi NetBeans tertentu, jalankan dan ubah layout langsung dari kode Java; semua UI tetap berupa JFrame terpisah.

## Menjalankan
1. Jalankan MySQL dari XAMPP.
2. Import `database/schema.sql` ke phpMyAdmin.
3. Buka folder proyek `KoperasiDesaKasir` di NetBeans 31.
4. Pilih JDK 21 dan tunggu Maven mengunduh dependency.
5. Jalankan `id.koperasidesa.Main`.

Terminal:
```bash
mvn clean compile
mvn exec:java
```

## Koneksi database default
Host `localhost`, port `3306`, database `koperasi_desa`, user `root`, password kosong. Sesuaikan `src/main/java/id/koperasidesa/config/Database.java` bila berbeda.

## Keamanan riwayat
Tabel `sale_items` menyimpan `product_code_snapshot`, `product_name_snapshot`, `unit_price_snapshot`, quantity dan subtotal. Tidak ada foreign key dari item riwayat ke tabel produk. Penghapusan barang adalah soft-delete (`active=0`), sehingga transaksi lama tidak berubah ketika katalog diedit/dihapus.

## Struktur OOP
`model` = data; `dao` = SQL; `service` = validasi checkout; `ui` = form dan tampilan; `config` = koneksi database.

Catatan: proyek ini disusun sebagai source project, namun perlu diuji build dan GUI Builder-nya di lingkungan NetBeans Anda. File `.form` disertakan untuk tiap JFrame; layout programatik menjaga source tetap mudah diedit.
