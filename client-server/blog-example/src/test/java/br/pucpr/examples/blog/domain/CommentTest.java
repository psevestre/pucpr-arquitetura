package br.pucpr.examples.blog.domain;

import static br.pucpr.examples.blog.domain.CommentTestSamples.*;
import static br.pucpr.examples.blog.domain.PostTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.pucpr.examples.blog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Comment.class);
        Comment comment1 = getCommentSample1();
        Comment comment2 = new Comment();
        assertThat(comment1).isNotEqualTo(comment2);

        comment2.setId(comment1.getId());
        assertThat(comment1).isEqualTo(comment2);

        comment2 = getCommentSample2();
        assertThat(comment1).isNotEqualTo(comment2);
    }

    @Test
    void postTest() {
        Comment comment = getCommentRandomSampleGenerator();
        Post postBack = getPostRandomSampleGenerator();

        comment.setPost(postBack);
        assertThat(comment.getPost()).isEqualTo(postBack);

        comment.post(null);
        assertThat(comment.getPost()).isNull();
    }
}
