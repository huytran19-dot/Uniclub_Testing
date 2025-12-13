import { API_URL } from '@/config/api'

const API_BASE_URL = API_URL;

// Categories API
export async function getCategories() {
  const response = await fetch(`${API_BASE_URL}/categories`)
  if (!response.ok) {
    throw new Error("Failed to fetch categories")
  }
  return response.json()
}

export async function getCategoryById(id) {
  const response = await fetch(`${API_BASE_URL}/categories/${id}`)
  if (!response.ok) {
    throw new Error("Failed to fetch category")
  }
  return response.json()
}

// Products API
export async function getProducts() {
  const response = await fetch(`${API_BASE_URL}/products`)
  if (!response.ok) {
    throw new Error("Failed to fetch products")
  }
  return response.json()
}

export async function getProductById(id) {
  const response = await fetch(`${API_BASE_URL}/products/${id}`)
  if (!response.ok) {
    throw new Error("Failed to fetch product")
  }
  return response.json()
}

export async function getProductsByCategory(categoryId) {
  const response = await fetch(`${API_BASE_URL}/products/category/${categoryId}`)
  if (!response.ok) {
    throw new Error("Failed to fetch products by category")
  }
  return response.json()
}

export async function getProductsByBrand(brandId) {
  const response = await fetch(`${API_BASE_URL}/products/brand/${brandId}`)
  if (!response.ok) {
    throw new Error("Failed to fetch products by brand")
  }
  return response.json()
}

export async function searchProducts(query) {
  const response = await fetch(`${API_BASE_URL}/products/search?q=${encodeURIComponent(query)}`)
  if (!response.ok) {
    throw new Error("Failed to search products")
  }
  return response.json()
}

// Brands API
export async function getBrands() {
  const response = await fetch(`${API_BASE_URL}/brands`)
  if (!response.ok) {
    throw new Error("Failed to fetch brands")
  }
  return response.json()
}

// Variants API
export async function getVariants() {
  const response = await fetch(`${API_BASE_URL}/variants`)
  if (!response.ok) {
    throw new Error("Failed to fetch variants")
  }
  return response.json()
}

export async function getVariantsByProduct(productId) {
  const response = await fetch(`${API_BASE_URL}/variants/product/${productId}`)
  if (!response.ok) {
    throw new Error("Failed to fetch variants by product")
  }
  return response.json()
}

// Colors API
export async function getColors() {
  const response = await fetch(`${API_BASE_URL}/colors`)
  if (!response.ok) {
    throw new Error("Failed to fetch colors")
  }
  return response.json()
}

// Sizes API
export async function getSizes() {
  const response = await fetch(`${API_BASE_URL}/sizes`)
  if (!response.ok) {
    throw new Error("Failed to fetch sizes")
  }
  return response.json()
}
