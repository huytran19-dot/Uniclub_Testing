"use client"

export interface CartItem {
  sku_variant: number
  productName: string
  sizeName: string
  colorName: string
  unitPrice: number
  qty: number
  image: string
  maxQuantity: number
}

const CART_KEY = "uniclub_cart"

export function getCart(): CartItem[] {
  if (typeof window === "undefined") return []
  const cart = localStorage.getItem(CART_KEY)
  return cart ? JSON.parse(cart) : []
}

export function saveCart(cart: CartItem[]): void {
  if (typeof window === "undefined") return
  localStorage.setItem(CART_KEY, JSON.stringify(cart))
}

export function addToCart(item: Omit<CartItem, "qty"> & { qty?: number }): void {
  const cart = getCart()
  const existingIndex = cart.findIndex((i) => i.sku_variant === item.sku_variant)

  if (existingIndex >= 0) {
    cart[existingIndex].qty = Math.min(cart[existingIndex].qty + (item.qty || 1), item.maxQuantity)
  } else {
    cart.push({ ...item, qty: item.qty || 1 })
  }

  saveCart(cart)
}

export function updateCartItemQty(sku_variant: number, qty: number): void {
  const cart = getCart()
  const item = cart.find((i) => i.sku_variant === sku_variant)
  if (item) {
    item.qty = Math.min(Math.max(1, qty), item.maxQuantity)
    saveCart(cart)
  }
}

export function removeFromCart(sku_variant: number): void {
  const cart = getCart().filter((i) => i.sku_variant !== sku_variant)
  saveCart(cart)
}

export function clearCart(): void {
  saveCart([])
}

export function getCartTotal(): number {
  return getCart().reduce((sum, item) => sum + item.unitPrice * item.qty, 0)
}

export function getCartCount(): number {
  return getCart().reduce((sum, item) => sum + item.qty, 0)
}
