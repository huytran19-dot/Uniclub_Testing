const BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api"

// Helper function to get auth token
const getAuthToken = () => {
  return localStorage.getItem('token')
}

// Helper function to get headers with auth
const getAuthHeaders = () => {
  const token = getAuthToken()
  return {
    'Content-Type': 'application/json',
    ...(token && { 'Authorization': `Bearer ${token}` })
  }
}


async function fetchAPI(endpoint, options = {}) {
  try {
    const response = await fetch(`${BASE_URL}${endpoint}`, {
      headers: getAuthHeaders(),
      ...options,
    })
    if (!response.ok) {
      if (response.status === 401) {
        // Token expired or invalid, redirect to login
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        window.location.href = '/login'
        throw new Error('Unauthorized')
      }
      
      // Try to get error message from response
      let errorMessage = `API error: ${response.status}`
      try {
        const errorData = await response.text()
        if (errorData) {
          errorMessage = errorData
        }
      } catch (e) {
        // If can't parse error, use default message
      }
      
      throw new Error(errorMessage)
    }
    return await response.json()
  } catch (error) {
    console.error("❌ API Error:", error.message)
    throw error
  }
}

export const api = {
  // Generic CRUD
  list: async (resource, params = {}) => {
    const query = new URLSearchParams(params).toString()
    const data = await fetchAPI(`/${resource}?${query}`)
    return data || []
  },

  get: async (resource, id) => {
    const data = await fetchAPI(`/${resource}/${id}`)
    return data
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
    return data || []
  },

  approveGrn: async (id) => {
    return await fetchAPI(`/grn-headers/${id}`, { 
      method: "PUT",
      body: JSON.stringify({ status: "COMPLETED" })
    })
  },

  getGrnDetails: async (id) => {
    const data = await fetchAPI(`/grn-details/grn-header/${id}`)
    return data || []
  },

  // Order specific API
  updateOrderStatus: async (orderId, data) => {
    return await fetchAPI(`/orders/${orderId}/status`, {
      method: "PUT",
      body: JSON.stringify(data)
    })
  },

  // Users API
  list: async (endpoint) => {
    const data = await fetchAPI(`/${endpoint}`)
    return data || []
  },

  get: async (endpoint, id) => {
    return await fetchAPI(`/${endpoint}/${id}`)
  },

  create: async (endpoint, data) => {
    return await fetchAPI(`/${endpoint}`, {
      method: "POST",
      body: JSON.stringify(data)
    })
  },

  update: async (endpoint, id, data) => {
    return await fetchAPI(`/${endpoint}/${id}`, {
      method: "PUT",
      body: JSON.stringify(data)
    })
  },

  delete: async (endpoint, id) => {
    return await fetchAPI(`/${endpoint}/${id}`, {
      method: "DELETE"
    })
  },
}
