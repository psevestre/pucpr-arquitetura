package br.pucpr.examples.blog.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CommentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Comment getCommentSample1() {
        return new Comment().id(1L).content("content1").userId("userId1");
    }

    public static Comment getCommentSample2() {
        return new Comment().id(2L).content("content2").userId("userId2");
    }

    public static Comment getCommentRandomSampleGenerator() {
        return new Comment().id(longCount.incrementAndGet()).content(UUID.randomUUID().toString()).userId(UUID.randomUUID().toString());
    }
}
