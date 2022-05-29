package kr.co.tmax.rabackend.config.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageResponseDate<T> {

    private final T elements;
    private final PageInfo pageInfo;

    public static <T> PageResponseDate<T> of(T elements, PageInfo pageInfo) {
        return new PageResponseDate<>(elements, pageInfo);
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PageInfo {
        private final int currentPageNumber;
        private final int lastPageNumber;

        private final int sizePerPage;
        private final long totalElementCount;

        private final Boolean isLastPage;
        private final Boolean isFirstPage;

        public static PageInfo of(Page<?> page) {
            return new PageInfo(
                    page.getPageable().getPageNumber(),
                    page.getTotalPages(),
                    page.getPageable().getPageSize(),
                    page.getTotalElements(),
                    page.isLast(),
                    page.isFirst()
            );
        }

    }
}
