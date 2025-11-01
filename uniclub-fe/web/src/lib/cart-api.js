import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

// Get or create cart for current user
export const getUserCart = async (userId) => {
  try {
    const response = await axios.get(`${API_URL}/carts/user/${userId}`);
    return response.data;
  } catch (error) {
    // If cart doesn't exist (404), create new cart
    if (error.response?.status === 404) {
      const createResponse = await axios.post(
        `${API_URL}/carts`,
        { userId, note: '' }
      );
      return createResponse.data;
    }
    throw error;
  }
};

// Get cart items
export const getCartItems = async (cartId) => {
  const response = await axios.get(`${API_URL}/cart-items/cart/${cartId}`);
  return response.data;
};

// Add item to cart
export const addToCart = async (cartId, variantSku, quantity, unitPrice) => {
  const response = await axios.post(
    `${API_URL}/cart-items`,
    {
      cartId,
      variantSku,
      quantity,
      unitPrice
    }
  );
  return response.data;
};

// Update cart item quantity
export const updateCartItem = async (cartItemId, quantity) => {
  const response = await axios.put(
    `${API_URL}/cart-items/${cartItemId}`,
    { quantity }
  );
  return response.data;
};

// Remove item from cart
export const removeCartItem = async (cartItemId) => {
  await axios.delete(`${API_URL}/cart-items/${cartItemId}`);
};

// Get full cart with items and product details
export const getFullCart = async (userId) => {
  try {
    // Get cart
    const cart = await getUserCart(userId);
    
    // Get cart items
    const cartItems = await getCartItems(cart.id);
    
    // Fetch product details for each item
    const itemsWithDetails = await Promise.all(
      cartItems.map(async (item) => {
        try {
          // Fetch variant details
          const variantRes = await axios.get(`${API_URL}/variants/${item.variantSku}`);
          const variant = variantRes.data;
          
          // Fetch product details
          const productRes = await axios.get(`${API_URL}/products/${variant.productId}`);
          const product = productRes.data;
          
          // Fetch size and color
          const [sizeRes, colorRes] = await Promise.all([
            axios.get(`${API_URL}/sizes/${variant.sizeId}`),
            axios.get(`${API_URL}/colors/${variant.colorId}`)
          ]);
          
          return {
            id: item.id,
            sku_variant: item.variantSku,
            productName: product.name,
            sizeName: sizeRes.data.name,
            colorName: colorRes.data.name,
            unitPrice: item.unitPrice,
            quantity: item.quantity,
            subtotal: item.subtotal,
            image: variant.images,
            maxQuantity: variant.quantity
          };
        } catch (error) {
          console.error('Error fetching item details:', error);
          return null;
        }
      })
    );
    
    return {
      cart,
      items: itemsWithDetails.filter(item => item !== null)
    };
  } catch (error) {
    console.error('Error getting full cart:', error);
    return { cart: null, items: [] };
  }
};
