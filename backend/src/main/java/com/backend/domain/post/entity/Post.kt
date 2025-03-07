package com.backend.domain.post.entity

import com.backend.domain.category.entity.Category
import com.backend.domain.comment.entity.Comment
import com.backend.domain.post.dto.FreePostRequest
import com.backend.domain.user.entity.SiteUser
import com.backend.domain.voter.entity.Voter
import com.backend.global.baseentity.BaseEntity
import com.querydsl.core.types.Projections.constructor
import jakarta.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "post_type")
@Table(name = "post")
class Post : BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    var postId: Long? = null
        protected set

    @Column(nullable = false)
    lateinit var subject: String

    @Column(nullable = false)
    lateinit var content: String

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    lateinit var category: Category
        protected set

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    lateinit var author: SiteUser
        protected set

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = [CascadeType.REMOVE])
    private var _postCommentList: MutableList<Comment> = mutableListOf()

    val postCommentList: List<Comment>
        get() = _postCommentList.toList()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = [CascadeType.REMOVE])
    private var _postVoterList: MutableList<Voter> = mutableListOf()

    val postVoterList: List<Voter>
        get() = _postVoterList.toList()

    // 게시글 수정 메서드
    fun updatePost(subject: String, content: String){
        this.subject = subject
        this.content = content
    }

    constructor(request: FreePostRequest, siteUser: SiteUser, category: Category) {
        this.subject = request.subject
        this.content = request.content
        this.category = category
        this.author = siteUser

    }
    constructor(targetId: Long)  {
        this.postId = targetId
    }

    constructor(subject: String, content: String, category: Category, author: SiteUser) : super() {
        this.subject = subject
        this.content = content
        this.category = category
        this.author = author
    }

}


