import React from "react"
import { PageLayout } from "../components/PageLayout"
import Hero from "@/components/home/Hero"
import FeaturedCategories from "@/components/home/FeaturedCategories"
import BestSellers from "@/components/home/BestSellers"
import PromoBanner from "@/components/home/PromoBanner"

export default function HomePage() {
  return (
    <PageLayout>
      <Hero />
      <FeaturedCategories />
      <BestSellers />
      <PromoBanner />
    </PageLayout>
  )
}
