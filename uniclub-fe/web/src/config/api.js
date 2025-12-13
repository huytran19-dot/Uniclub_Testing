// ============================================
// API Configuration - Single Source of Truth
// ============================================
// Usage: import { API_URL } from '@/config/api'
// Set VITE_API_URL in .env file to override default
// ============================================

export const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

// Export default for convenience
export default {
  API_URL,
  BASE_URL: API_URL,
}
