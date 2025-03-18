package br.pucpr.examples.blog.domain;

import static br.pucpr.examples.blog.domain.AuthorTestSamples.*;
import static br.pucpr.examples.blog.domain.PostTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.pucpr.examples.blog.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AuthorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Author.class);
        Author author1 = getAuthorSample1();
        Author author2 = new Author();
        assertThat(author1).isNotEqualTo(author2);

        author2.setId(author1.getId());
        assertThat(author1).isEqualTo(author2);

        author2 = getAuthorSample2();
        assertThat(author1).isNotEqualTo(author2);
    }

    @Test
    void postTest() {
        Author author = getAuthorRandomSampleGenerator();
        Post postBack = getPostRandomSampleGenerator();

        author.addPost(postBack);
        assertThat(author.getPosts()).containsOnly(postBack);
        assertThat(postBack.getAuthor()).isEqualTo(author);

        author.removePost(postBack);
        assertThat(author.getPosts()).doesNotContain(postBack);
        assertThat(postBack.getAuthor()).isNull();

        author.posts(new HashSet<>(Set.of(postBack)));
        assertThat(author.getPosts()).containsOnly(postBack);
        assertThat(postBack.getAuthor()).isEqualTo(author);

        author.setPosts(new HashSet<>());
        assertThat(author.getPosts()).doesNotContain(postBack);
        assertThat(postBack.getAuthor()).isNull();
    }
}
