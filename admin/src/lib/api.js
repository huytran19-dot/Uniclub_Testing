const BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api"

// Mock data fallback
const mockData = {
  categories: [
    { id: 1, name: "Áo Nam", status: 1, created_at: "2025-10-01" },
    { id: 2, name: "Áo Nữ", status: 1, created_at: "2025-10-02" },
  ],
  brands: [
    { id: 1, name: "Uniqlo", status: 1, created_at: "2025-10-01" },
    { id: 2, name: "Nike", status: 1, created_at: "2025-10-02" },
  ],
  sizes: [
    { id: 1, name: "XS", status: 1 },
    { id: 2, name: "S", status: 1 },
    { id: 3, name: "M", status: 1 },
    { id: 4, name: "L", status: 1 },
    { id: 5, name: "XL", status: 1 },
  ],
  colors: [
    { id: 1, name: "Đen", hex_code: "#000000", status: 1 },
    { id: 2, name: "Trắng", hex_code: "#FFFFFF", status: 1 },
    { id: 3, name: "Xanh", hex_code: "#0066CC", status: 1 },
  ],
  products: [
    {
      id: 1,
      name: "Áo thun cổ tròn",
      description: "Áo thun chất lượng cao",
      information: "Chất liệu: Cotton 100%",
      id_brand: 1,
      id_category: 1,
      status: 1,
      created_at: "2025-10-10",
    },
  ],
  variants: [
    {
      sku: 1001,
      id_product: 1,
      id_color: 1,
      id_size: 3,
      images: "/shirt.jpg",
      quantity: 15,
      price: 199000,
      status: 1,
      created_at: "2025-10-12",
    },
  ],
  orders: [
    {
      id: 1,
      id_user: 2,
      total: 398000,
      note: "Giao nhanh",
      status: "PENDING",
      created_at: "2025-10-15",
    },
  ],
  suppliers: [
    {
      id: 1,
      name: "Dệt May Sài Gòn",
      contact_person: "Anh Phú",
      phone: "0909123456",
      email: "ncc@sg.com",
      address: "Q1, TP.HCM",
      status: 1,
      created_at: "2025-10-01",
    },
  ],
  grn: {
    headers: [
      {
        id: 1,
        id_supplier: 1,
        total_cost: 5000000,
        note: "Phiếu nhập hàng",
        received_date: "2025-10-15",
        status: "PENDING",
        created_at: "2025-10-15",
      },
    ],
    details: [
      {
        id: 11,
        id_grn: 1,
        id_variant: 1001,
        quantity: 20,
        unit_cost: 120000,
        subtotal: 2400000,
      },
    ],
  },
}

async function fetchAPI(endpoint, options = {}) {
  try {
    const response = await fetch(`${BASE_URL}${endpoint}`, {
      headers: { "Content-Type": "application/json", ...options.headers },
      ...options,
    })
    if (!response.ok) throw new Error(`API error: ${response.status}`)
    return await response.json()
  } catch (error) {
    console.warn("⚠️ API not available, using mock data:", error.message)
    return null
  }
}

export const api = {
  // Generic CRUD
  list: async (resource, params = {}) => {
    const query = new URLSearchParams(params).toString()
    const data = await fetchAPI(`/${resource}?${query}`)
    return data || mockData[resource] || []
  },

  get: async (resource, id) => {
    const data = await fetchAPI(`/${resource}/${id}`)
    if (data) return data
    if (resource === "grn") return mockData.grn.headers.find((h) => h.id === Number.parseInt(id))
    return mockData[resource]?.find((item) => item.id === Number.parseInt(id))
  },

  create: async (resource, payload) => {
    return await fetchAPI(`/${resource}`, {
      method: "POST",
      body: JSON.stringify(payload),
    })
  },

  update: async (resource, id, payload) => {
    return await fetchAPI(`/${resource}/${id}`, {
      method: "PUT",
      body: JSON.stringify(payload),
    })
  },

  delete: async (resource, id) => {
    return await fetchAPI(`/${resource}/${id}`, { method: "DELETE" })
  },

  // Special endpoints
  searchVariants: async (keyword) => {
    const data = await fetchAPI(`/variants/search?q=${keyword}`)
    return (
      data ||
      mockData.variants.filter((v) => v.sku.toString().includes(keyword) || v.id_product.toString().includes(keyword))
    )
  },

  approveGrn: async (id) => {
    return await fetchAPI(`/grn/${id}/approve`, { method: "POST" })
  },

  getGrnDetails: async (id) => {
    const data = await fetchAPI(`/grn/${id}/details`)
    return data || mockData.grn.details.filter((d) => d.id_grn === Number.parseInt(id))
  },
}
