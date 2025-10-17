import { clsx } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs) {
  return twMerge(clsx(inputs))
}

// Format currency in Vietnamese Dong
export function formatPrice(price) {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  })
    .format(price)
    .replace("₫", "đ")
}

// Calculate average rating
export function calculateAverageRating(reviews) {
  if (reviews.length === 0) return 0
  const sum = reviews.reduce((acc, review) => acc + review.star, 0)
  return Math.round((sum / reviews.length) * 10) / 10
}

// Get min price from variants
export function getMinPrice(variants, fallbackPrice) {
  const availableVariants = variants.filter((v) => v.quantity > 0 && v.price !== null)
  if (availableVariants.length === 0) return fallbackPrice
  return Math.min(...availableVariants.map((v) => v.price))
}

// Check if product is out of stock
export function isOutOfStock(variants) {
  return variants.every((v) => v.quantity === 0)
}
