package com.cuctut.hl.common;

import com.cuctut.hl.model.entity.DailyQuestion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum BucketNameEnum {

    MATERIALS_BUCKET("materials"),
    EXERCISES_BUCKET("exercises"),
    DQ_BUCKET("daily-questions");

    private final String BucketName;

    public static boolean mapBucketName(String s) {
        return Arrays.stream(BucketNameEnum.values())
                .anyMatch(bucket -> bucket.getBucketName().equals(s));
    }
}
