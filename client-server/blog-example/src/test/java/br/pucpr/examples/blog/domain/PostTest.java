package br.pucpr.examples.blog.domain;

import static br.pucpr.examples.blog.domain.AuthorTestSamples.*;
import static br.pucpr.examples.blog.domain.CommentTestSamples.*;
import static br.pucpr.examples.blog.domain.PostTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.pucpr.examples.blog.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PostTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Post.class);
        Post post1 = getPostSample1();
        Post post2 = new Post();
        assertThat(post1).isNotEqualTo(post2);

        post2.setId(post1.getId());
        assertThat(post1).isEqualTo(post2);

        post2 = getPostSample2();
        assertThat(post1).isNotEqualTo(post2);
    }

    @Test
    void commentTest() {
        Post post = getPostRandomSampleGenerator();
        Comment commentBack = getCommentRandomSampleGenerator();

        post.addComment(commentBack);
        assertThat(post.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getPost()).isEqualTo(post);

        post.removeComment(commentBack);
        assertThat(post.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getPost()).isNull();

        post.comments(new HashSet<>(Set.of(commentBack)));
        assertThat(post.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getPost()).isEqualTo(post);

        post.setComments(new HashSet<>());
        assertThat(post.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getPost()).isNull();
    }

    @Test
    void authorTest() {
        Post post = getPostRandomSampleGenerator();
        Author authorBack = getAuthorRandomSampleGenerator();

        post.setAuthor(authorBack);
        assertThat(post.getAuthor()).isEqualTo(authorBack);

        post.author(null);
        assertThat(post.getAuthor()).isNull();
    }
}
