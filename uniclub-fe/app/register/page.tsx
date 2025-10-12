"use client"

import type React from "react"

import { useState, useEffect } from "react"
import { useRouter } from "next/navigation"
import Link from "next/link"
import { PageLayout } from "@/components/page-layout"
import { FormField } from "@/components/form-field"
import { Button } from "@/components/ui/button"
import { register, getCurrentUser } from "@/lib/auth"

export default function RegisterPage() {
  const router = useRouter()
  const [mounted, setMounted] = useState(false)
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [fullName, setFullName] = useState("")
  const [error, setError] = useState("")

  useEffect(() => {
    setMounted(true)
    // Redirect if already logged in
    if (getCurrentUser()) {
      router.push("/")
    }
  }, [router])

  if (!mounted) return null

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    setError("")

    if (!email || !password || !fullName) {
      setError("Vui lòng nhập đầy đủ thông tin")
      return
    }

    if (password.length < 6) {
      setError("Mật khẩu phải có ít nhất 6 ký tự")
      return
    }

    const user = register(email, password, fullName)

    if (!user) {
      setError("Email đã được sử dụng")
      return
    }

    window.dispatchEvent(new Event("auth-updated"))
    router.push("/")
  }

  return (
    <PageLayout title="Đăng ký" breadcrumbs={[{ label: "Đăng ký" }]}>
      <div className="max-w-md mx-auto">
        <div className="bg-white rounded-2xl border border-border p-8 shadow-sm">
          <h2 className="text-2xl font-bold text-foreground mb-6 text-center">Đăng ký tài khoản</h2>

          <form onSubmit={handleSubmit} className="space-y-4">
            <FormField
              name="full_name"
              label="Họ và tên"
              value={fullName}
              onChange={setFullName}
              placeholder="Nguyễn Văn A"
              required
            />

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
              Đăng ký
            </Button>
          </form>

          <div className="mt-6 text-center text-sm text-muted-foreground">
            Đã có tài khoản?{" "}
            <Link href="/login" className="text-primary font-medium hover:underline">
              Đăng nhập
            </Link>
          </div>
        </div>
      </div>
    </PageLayout>
  )
}
