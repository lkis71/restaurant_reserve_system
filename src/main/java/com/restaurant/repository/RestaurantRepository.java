package com.restaurant.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.restaurant.controller.dto.MyRestaurantDto;
import com.restaurant.controller.response.QRestaurantResponse;
import com.restaurant.controller.response.RestaurantResponse;
import com.restaurant.entity.QFileMaster;
import com.restaurant.entity.Restaurant;
import com.restaurant.entity.RestaurantFile;
import com.restaurant.entity.type.UseType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static com.restaurant.entity.QFileMaster.fileMaster;
import static com.restaurant.entity.QRestaurant.restaurant;
import static com.restaurant.entity.QRestaurantFile.restaurantFile;

@Repository
@RequiredArgsConstructor
public class RestaurantRepository {

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 식당 등록
     *
     * @param restaurant
     */
    public void save(Restaurant restaurant) {
        em.persist(restaurant);
    }

    public void saveFile(RestaurantFile restaurantFile) {
        em.persist(restaurantFile);
    }

    /**
     * 식당 조회(단건)
     *
     * @param restaurantId 식당아이디
     * @return
     */
    public Restaurant findOne(Long restaurantId) {
        return jpaQueryFactory.selectFrom(restaurant)
                .leftJoin(restaurant.restaurantFile, restaurantFile)
                .fetchJoin()
                .leftJoin(restaurantFile.fileMaster, fileMaster)
                .fetchJoin()
                .where(restaurant.id.eq(restaurantId))
                .fetchOne();
    }

    /**
     * 등록 된 전체 식당 수
     *
     * @return
     */
    public int count() {
        return jpaQueryFactory.selectFrom(restaurant)
                .leftJoin(restaurant.restaurantFile, restaurantFile)
                .fetchJoin()
                .where(restaurant.useType.eq(UseType.USE)
                .and(restaurant.restaurantFile.useType.eq(UseType.USE)))
                .fetch().size();
    }

    /**
     * 식당목록 페이징 조회
     *
     * @param cursor 커서번호
     * @param limit 한 페이지에 보여질 목록 수
     * @return
     */
    public List<RestaurantResponse> findByPaging(Long cursor, int limit) {
        return jpaQueryFactory.select(new QRestaurantResponse(
                        restaurant.id,
                        restaurant.restaurantName,
                        restaurant.address,
                        restaurant.contact,
                        restaurant.content,
                        restaurant.restaurantType,
                        fileMaster.id,
                        fileMaster.fileName)
                )
                .from(restaurant)
                .leftJoin(restaurant.restaurantFile, restaurantFile).on(restaurantFile.useType.eq(UseType.USE))
                .leftJoin(restaurantFile.fileMaster, fileMaster).on(fileMaster.useType.eq(UseType.USE))
                .where(cursorId(cursor)
                .and(restaurant.useType.eq(UseType.USE)))
                .orderBy(restaurant.id.asc())
                .limit(limit)
                .fetch();
    }

    private BooleanExpression cursorId(Long cursorId){
        return cursorId == null ? null : restaurant.id.gt(cursorId);
    }

    public List<MyRestaurantDto> findRestaurantByMemberId(String memberId) {
        return jpaQueryFactory
                .select(Projections.constructor(MyRestaurantDto.class,
                    restaurant.id,
                    restaurant.restaurantName,
                    restaurant.contact)
                )
                .from(restaurant)
                .where(restaurant.member.memberId.eq(memberId))
                .fetch();
    }
}
