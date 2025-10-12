"use client"

import type React from "react"

import { useState, useEffect } from "react"
import { useRouter, useSearchParams } from "next/navigation"
import Link from "next/link"
import { PageLayout } from "@/components/page-layout"
import { FormField } from "@/components/form-field"
import { Button } from "@/components/ui/button"
import { login, getCurrentUser } from "@/lib/auth"

export default function LoginPage() {
  const router = useRouter()
  const searchParams = useSearchParams()
  const [mounted, setMounted] = useState(false)
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [error, setError] = useState("")

  useEffect(() => {
    setMounted(true)
    // Redirect if already logged in
    if (getCurrentUser()) {
      const redirect = searchParams.get("redirect") || "/"
      router.push(redirect)
    }
  }, [router, searchParams])

  if (!mounted) return null

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    setError("")

    if (!email || !password) {
      setError("Vui lòng nhập đầy đủ thông tin")
      return
    }

    const user = login(email, password)

    if (!user) {
      setError("Email hoặc mật khẩu không đúng")
      return
    }

    window.dispatchEvent(new Event("auth-updated"))
    const redirect = searchParams.get("redirect") || "/"
    router.push(redirect)
  }

  return (
    <PageLayout title="Đăng nhập" breadcrumbs={[{ label: "Đăng nhập" }]}>
      <div className="max-w-md mx-auto">
        <div className="bg-white rounded-2xl border border-border p-8 shadow-sm">
          <h2 className="text-2xl font-bold text-foreground mb-6 text-center">Đăng nhập</h2>

          <form onSubmit={handleSubmit} className="space-y-4">
            <FormField
              name="email"
              label="Email"
              type="email"
              value={email}
              onChange={setEmail}
              placeholder="your@email.com"
              required
            />

            <FormField
              name="password"
              label="Mật khẩu"
              type="text"
              value={password}
              onChange={setPassword}
              placeholder="••••••••"
              required
            />

            {error && (
              <div className="bg-danger/10 border border-danger text-danger px-4 py-3 rounded-lg text-sm">{error}</div>
            )}

            <Button type="submit" size="lg" className="w-full">
              Đăng nhập
            </Button>
          </form>

          <div className="mt-6 text-center text-sm text-muted-foreground">
            Chưa có tài khoản?{" "}
            <Link href="/register" className="text-primary font-medium hover:underline">
              Đăng ký ngay
            </Link>
          </div>

          <div className="mt-4 p-4 bg-surface rounded-lg text-sm text-muted-foreground">
            <p className="font-medium mb-2">Tài khoản demo:</p>
            <p>Email: user@uniclub.vn</p>
            <p>Mật khẩu: user123</p>
          </div>
        </div>
      </div>
    </PageLayout>
  )
}
