import React, { useState } from "react"
import { Star, ThumbsUp, MessageCircle, Image as ImageIcon } from "lucide-react"
import { Button } from "@/components/ui/button"
import { getCurrentUser } from "@/lib/auth"
import { ReviewForm } from "./ReviewForm"
import { CommentSection } from "./CommentSection"

export function ProductReviews({ productId, reviews = [], onAddReview }) {
  const [showReviewForm, setShowReviewForm] = useState(false)
  const [expandedReview, setExpandedReview] = useState(null)
  const user = getCurrentUser()

  const averageRating = reviews.length > 0
    ? (reviews.reduce((sum, r) => sum + r.star, 0) / reviews.length).toFixed(1)
    : 0

  const ratingDistribution = [5, 4, 3, 2, 1].map((star) => ({
    star,
    count: reviews.filter((r) => r.star === star).length,
    percentage: reviews.length > 0 ? (reviews.filter((r) => r.star === star).length / reviews.length) * 100 : 0,
  }))

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString("vi-VN", {
      year: "numeric",
      month: "long",
      day: "numeric",
    })
  }

  const renderStars = (rating) => {
    return (
      <div className="flex gap-0.5">
        {[1, 2, 3, 4, 5].map((star) => (
          <Star
            key={star}
            className={`w-4 h-4 ${
              star <= rating ? "fill-warning text-warning" : "text-muted"
            }`}
          />
        ))}
      </div>
    )
  }

  return (
    <div className="space-y-6">
      {/* Rating Summary */}
      <div className="card p-6">
        <h3 className="text-xl font-bold text-foreground mb-4">Đánh giá sản phẩm</h3>
        <div className="grid md:grid-cols-2 gap-6">
          {/* Average Rating */}
          <div className="flex flex-col items-center justify-center p-6 bg-muted/30 rounded-xl">
            <div className="text-5xl font-bold text-foreground mb-2">{averageRating}</div>
            <div className="flex gap-1 mb-2">
              {renderStars(Math.round(averageRating))}
            </div>
            <div className="text-sm text-muted-foreground">
              {reviews.length} đánh giá
            </div>
          </div>

          {/* Rating Distribution */}
          <div className="space-y-2">
            {ratingDistribution.map(({ star, count, percentage }) => (
              <div key={star} className="flex items-center gap-3">
                <div className="flex items-center gap-1 w-16">
                  <span className="text-sm font-medium">{star}</span>
                  <Star className="w-3 h-3 fill-warning text-warning" />
                </div>
                <div className="flex-1 h-2 bg-muted rounded-full overflow-hidden">
                  <div
                    className="h-full bg-warning transition-all"
                    style={{ width: `${percentage}%` }}
                  />
                </div>
                <span className="text-sm text-muted-foreground w-12 text-right">
                  {count}
                </span>
              </div>
            ))}
          </div>
        </div>

        {/* Write Review Button */}
        {user && (
          <div className="mt-6 pt-6 border-t border-border">
            {!showReviewForm ? (
              <Button onClick={() => setShowReviewForm(true)} className="w-full">
                Viết đánh giá
              </Button>
            ) : (
              <ReviewForm
                productId={productId}
                onSubmit={(review) => {
                  onAddReview(review)
                  setShowReviewForm(false)
                }}
                onCancel={() => setShowReviewForm(false)}
              />
            )}
          </div>
        )}
      </div>

      {/* Reviews List */}
      <div className="space-y-4">
        {reviews.length === 0 ? (
          <div className="card p-8 text-center">
            <MessageCircle className="w-12 h-12 mx-auto text-muted-foreground mb-3" />
            <div className="text-muted-foreground">
              Chưa có đánh giá nào. Hãy là người đầu tiên đánh giá sản phẩm này!
            </div>
          </div>
        ) : (
          reviews.map((review) => (
            <div key={review.id} className="card p-6">
              <div className="flex items-start gap-4">
                {/* User Avatar */}
                <div
                  className="w-10 h-10 rounded-full flex items-center justify-center text-white font-semibold flex-shrink-0"
                  style={{ backgroundColor: "hsl(217.2 91.2% 55%)" }}
                >
                  {review.user_name?.charAt(0).toUpperCase() || "U"}
                </div>

                <div className="flex-1 min-w-0">
                  {/* Header */}
                  <div className="flex items-start justify-between gap-2 mb-2">
                    <div>
                      <div className="font-semibold text-foreground">
                        {review.user_name || "Người dùng"}
                      </div>
                      <div className="flex items-center gap-2 mt-1">
                        {renderStars(review.star)}
                        <span className="text-xs text-muted-foreground">
                          {formatDate(review.created_at)}
                        </span>
                      </div>
                    </div>
                  </div>

                  {/* Content */}
                  <p className="text-foreground mb-3">{review.content}</p>

                  {/* Images */}
                  {review.images && (
                    <div className="flex gap-2 mb-3">
                      {review.images.split(",").map((img, idx) => (
                        <div
                          key={idx}
                          className="w-20 h-20 rounded-lg overflow-hidden border border-border"
                        >
                          <img
                            src={img.trim()}
                            alt={`Review ${idx + 1}`}
                            className="w-full h-full object-cover"
                          />
                        </div>
                      ))}
                    </div>
                  )}

                  {/* Actions */}
                  <div className="flex items-center gap-4">
                    <button className="flex items-center gap-1 text-sm text-muted-foreground hover:text-primary transition-colors">
                      <ThumbsUp className="w-4 h-4" />
                      <span>Hữu ích</span>
                    </button>
                    <button
                      onClick={() =>
                        setExpandedReview(expandedReview === review.id ? null : review.id)
                      }
                      className="flex items-center gap-1 text-sm text-muted-foreground hover:text-primary transition-colors"
                    >
                      <MessageCircle className="w-4 h-4" />
                      <span>Bình luận</span>
                    </button>
                  </div>

                  {/* Comment Section */}
                  {expandedReview === review.id && (
                    <div className="mt-4 pt-4 border-t border-border">
                      <CommentSection reviewId={review.id} />
                    </div>
                  )}
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  )
}
