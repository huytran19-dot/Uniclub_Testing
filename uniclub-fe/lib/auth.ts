"use client"

import { users, roles } from "./mock-data"

export interface User {
  id: number
  email: string
  full_name: string
  id_role: number
  roleName: string
}

const AUTH_KEY = "uniclub_user"

export function login(email: string, password: string): User | null {
  const user = users.find((u) => u.email === email && u.password === password && u.status === 1)
  if (!user) return null

  const role = roles.find((r) => r.id === user.id_role)
  const authUser: User = {
    id: user.id,
    email: user.email,
    full_name: user.full_name,
    id_role: user.id_role,
    roleName: role?.name || "CUSTOMER",
  }

  if (typeof window !== "undefined") {
    localStorage.setItem(AUTH_KEY, JSON.stringify(authUser))
  }

  return authUser
}

export function register(email: string, password: string, full_name: string): User | null {
  // Check if email exists
  if (users.some((u) => u.email === email)) {
    return null
  }

  const customerRole = roles.find((r) => r.name === "CUSTOMER")
  const newUser = {
    id: users.length + 1,
    email,
    password,
    full_name,
    id_role: customerRole?.id || 2,
    status: 1,
    created_at: new Date().toISOString(),
    updated_at: new Date().toISOString(),
  }

  users.push(newUser)

  const authUser: User = {
    id: newUser.id,
    email: newUser.email,
    full_name: newUser.full_name,
    id_role: newUser.id_role,
    roleName: "CUSTOMER",
  }

  if (typeof window !== "undefined") {
    localStorage.setItem(AUTH_KEY, JSON.stringify(authUser))
  }

  return authUser
}

export function logout(): void {
  if (typeof window !== "undefined") {
    localStorage.removeItem(AUTH_KEY)
  }
}

export function getCurrentUser(): User | null {
  if (typeof window === "undefined") return null
  const user = localStorage.getItem(AUTH_KEY)
  return user ? JSON.parse(user) : null
}

export function isAuthenticated(): boolean {
  return getCurrentUser() !== null
}
