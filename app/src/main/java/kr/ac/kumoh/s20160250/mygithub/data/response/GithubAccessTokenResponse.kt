package kr.ac.kumoh.s20160250.mygithub.data.response

data class GithubAccessTokenResponse(
    val accessToken: String,
    val scope: String,
    val tokenType: String
)
