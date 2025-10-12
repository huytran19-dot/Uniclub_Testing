// Mock database with exact field names from schema

export const sizes = [
  { id: 1, name: "S", status: 1, created_at: "2024-01-01", updated_at: "2024-01-01" },
  { id: 2, name: "M", status: 1, created_at: "2024-01-01", updated_at: "2024-01-01" },
  { id: 3, name: "L", status: 1, created_at: "2024-01-01", updated_at: "2024-01-01" },
  { id: 4, name: "XL", status: 1, created_at: "2024-01-01", updated_at: "2024-01-01" },
  { id: 5, name: "XXL", status: 1, created_at: "2024-01-01", updated_at: "2024-01-01" },
]

export const colors = [
  { id: 1, name: "Black", status: 1, created_at: "2024-01-01", updated_at: "2024-01-01" },
  { id: 2, name: "White", status: 1, created_at: "2024-01-01", updated_at: "2024-01-01" },
  { id: 3, name: "Navy", status: 1, created_at: "2024-01-01", updated_at: "2024-01-01" },
  { id: 4, name: "Red", status: 1, created_at: "2024-01-01", updated_at: "2024-01-01" },
]

export const brands = [
  { id: 1, name: "UniClub", status: 1, created_at: "2024-01-01", updated_at: "2024-01-01" },
  { id: 2, name: "Nike", status: 1, created_at: "2024-01-01", updated_at: "2024-01-01" },
  { id: 3, name: "Adidas", status: 1, created_at: "2024-01-01", updated_at: "2024-01-01" },
]

export const categories = [
  { id: 1, name: "Men", status: 1, created_at: "2024-01-01", updated_at: "2024-01-01" },
  { id: 2, name: "Women", status: 1, created_at: "2024-01-01", updated_at: "2024-01-01" },
  { id: 3, name: "Accessories", status: 1, created_at: "2024-01-01", updated_at: "2024-01-01" },
]

export const roles = [
  { id: 1, name: "ADMIN", description: "Admin role", status: 1, created_at: "2024-01-01", updated_at: "2024-01-01" },
  {
    id: 2,
    name: "CUSTOMER",
    description: "Customer role",
    status: 1,
    created_at: "2024-01-01",
    updated_at: "2024-01-01",
  },
]

export const users = [
  {
    id: 1,
    email: "admin@uniclub.vn",
    password: "admin123",
    full_name: "Admin",
    id_role: 1,
    status: 1,
    created_at: "2024-01-01",
    updated_at: "2024-01-01",
  },
  {
    id: 2,
    email: "user@uniclub.vn",
    password: "user123",
    full_name: "User Demo",
    id_role: 2,
    status: 1,
    created_at: "2024-01-01",
    updated_at: "2024-01-01",
  },
]

export const payment_methods = [
  {
    id: 1,
    name: "COD",
    description: "Thanh toán khi nhận hàng",
    status: 1,
    created_at: "2024-01-01",
    updated_at: "2024-01-01",
  },
  {
    id: 2,
    name: "VNPAY",
    description: "Thanh toán trực tuyến",
    status: 1,
    created_at: "2024-01-01",
    updated_at: "2024-01-01",
  },
  { id: 3, name: "MOMO", description: "Ví MoMo", status: 1, created_at: "2024-01-01", updated_at: "2024-01-01" },
]

export const products = [
  {
    id: 1,
    name: "T-Shirt Basic",
    description: "Áo thun cotton cơ bản",
    information: "100% cotton",
    price: 250000,
    id_brand: 1,
    id_category: 1,
    status: 1,
    created_at: "2024-01-01",
    updated_at: "2024-01-01",
  },
  {
    id: 2,
    name: "Polo Premium",
    description: "Áo polo dáng slim",
    information: "Coolmax",
    price: 350000,
    id_brand: 3,
    id_category: 1,
    status: 1,
    created_at: "2024-01-02",
    updated_at: "2024-01-02",
  },
  {
    id: 3,
    name: "Women Hoodie",
    description: "Hoodie ấm áp",
    information: "Fleece",
    price: 450000,
    id_brand: 2,
    id_category: 2,
    status: 1,
    created_at: "2024-01-03",
    updated_at: "2024-01-03",
  },
  {
    id: 4,
    name: "Cap Street",
    description: "Nón lưỡi trai",
    information: "Polyester",
    price: 150000,
    id_brand: 1,
    id_category: 3,
    status: 1,
    created_at: "2024-01-04",
    updated_at: "2024-01-04",
  },
  {
    id: 5,
    name: "Jogger Men",
    description: "Quần jogger co giãn",
    information: "Spandex",
    price: 390000,
    id_brand: 2,
    id_category: 1,
    status: 1,
    created_at: "2024-01-05",
    updated_at: "2024-01-05",
  },
  {
    id: 6,
    name: "Skirt A-line",
    description: "Chân váy A-line",
    information: "Cotton mix",
    price: 420000,
    id_brand: 3,
    id_category: 2,
    status: 1,
    created_at: "2024-01-06",
    updated_at: "2024-01-06",
  },
]

export const variants = [
  {
    sku: 101,
    id_product: 1,
    id_size: 2,
    id_color: 1,
    images: "/black-basic-tshirt.png",
    quantity: 15,
    price: 250000,
    status: 1,
    created_at: "2024-01-01",
    updated_at: "2024-01-01",
  },
  {
    sku: 102,
    id_product: 1,
    id_size: 3,
    id_color: 2,
    images: "/white-basic-tshirt.png",
    quantity: 0,
    price: 250000,
    status: 1,
    created_at: "2024-01-01",
    updated_at: "2024-01-01",
  },
  {
    sku: 103,
    id_product: 2,
    id_size: 2,
    id_color: 3,
    images: "/navy-polo-shirt.jpg",
    quantity: 10,
    price: 355000,
    status: 1,
    created_at: "2024-01-02",
    updated_at: "2024-01-02",
  },
  {
    sku: 104,
    id_product: 3,
    id_size: 3,
    id_color: 4,
    images: "/red-women-hoodie.jpg",
    quantity: 8,
    price: 460000,
    status: 1,
    created_at: "2024-01-03",
    updated_at: "2024-01-03",
  },
  {
    sku: 105,
    id_product: 4,
    id_size: 4,
    id_color: 1,
    images: "/black-street-cap.jpg",
    quantity: 20,
    price: 150000,
    status: 1,
    created_at: "2024-01-04",
    updated_at: "2024-01-04",
  },
  {
    sku: 106,
    id_product: 5,
    id_size: 3,
    id_color: 1,
    images: "/black-jogger-pants.jpg",
    quantity: 6,
    price: 395000,
    status: 1,
    created_at: "2024-01-05",
    updated_at: "2024-01-05",
  },
  {
    sku: 107,
    id_product: 6,
    id_size: 2,
    id_color: 2,
    images: "/white-aline-skirt.jpg",
    quantity: 4,
    price: 425000,
    status: 1,
    created_at: "2024-01-06",
    updated_at: "2024-01-06",
  },
]

export const reviews = [
  {
    id: 1,
    id_product: 1,
    id_user: 2,
    star: 5,
    content: "Chất vải tốt, lên form chuẩn",
    images: "",
    status: 1,
    created_at: "2024-01-10",
    updated_at: "2024-01-10",
  },
  {
    id: 2,
    id_product: 2,
    id_user: 2,
    star: 4,
    content: "Mặc mát, thấm hút",
    images: "",
    status: 1,
    created_at: "2024-01-11",
    updated_at: "2024-01-11",
  },
  {
    id: 3,
    id_product: 3,
    id_user: 2,
    star: 3,
    content: "Hơi rộng, nhưng ấm",
    images: "",
    status: 1,
    created_at: "2024-01-12",
    updated_at: "2024-01-12",
  },
]

export const orders = [
  {
    id: 1001,
    total: 500000,
    note: "Giao giờ hành chính",
    id_payment: 2,
    id_user: 2,
    status: "PENDING",
    created_at: "2024-01-15",
    updated_at: "2024-01-15",
  },
]

export const order_variants = [{ id_order: 1001, sku_variant: 101, quantity: 2, price: 250000 }]

export const billing_details = [
  {
    id: 1,
    id_order: 1001,
    full_name: "Nguyễn Văn A",
    phone: "0901234567",
    email: "user@uniclub.vn",
    address: "123 Lê Lợi",
    province: "TP HCM",
    district: "Quận 1",
    ward: "Bến Thành",
    note: "Gọi trước",
    status: 1,
    created_at: "2024-01-15",
    updated_at: "2024-01-15",
  },
]
