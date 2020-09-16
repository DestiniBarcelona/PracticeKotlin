package com.example.demo

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@WebMvcTest
class HttpControllersTests(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    private lateinit var userRepository: UserRepository

    @MockkBean
    private lateinit var articleRepository: ArticleRepository

    @Test
    fun `List articles`() {
        val juergen = User("springjuergen", "Juer", "Gen")
        val spring5Article = Article("Spring goes OFF", "Dear Spring sistas...",
                "Lorem ipsum", juergen)
        val spring43Article = Article("Spring has the tea", "Dear Spring sistas...",
                "Lorem ipsum", juergen)
         every { articleRepository.findAllByOrderByAddedAtDesc() } returns
                listOf(spring5Article, spring43Article)
        mockMvc.perform(get("/api/article").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("\$.[0].author.login").value(juergen.login))
                .andExpect(jsonPath("\$.[0].slug").value(spring5Article.slug))
                .andExpect(jsonPath("\$.[1].author.login").value(juergen.login))
                .andExpect(jsonPath("\$.[1].slug").value(spring43Article.slug))
    }

    @Test
    fun `List users`() {
        val juergen = User("springjuergen", "Juer", "Gen")
        val smaldini = User("smaldini", "Smal", "Dini")
        every { userRepository.findAll() } returns listOf(juergen, smaldini)
        mockMvc.perform(get("/api/user/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("\$.[0].login").value(juergen.login))
                .andExpect(jsonPath("\$.[1].login").value(smaldini.login))
    }
}