package kr.ac.kumoh.s20160250.mygithub.data.response

import kr.ac.kumoh.s20160250.mygithub.data.entity.GithubRepoEntity

data class GithubRepoSearchResponse(
    val totalCount: Int,
    val Items: List<GithubRepoEntity>
)
