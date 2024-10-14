package com.friendbook1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.friendbook1.entities.UserFollowing;

import jakarta.transaction.Transactional;

import java.util.List;

public interface UserFollowingRepository extends JpaRepository<UserFollowing, Integer> {

	@Transactional
	void deleteByUserNameAndFollowingUserName(String userName, String followingUserName);

	long countByFollowingUserNameAndStatus(String followerUserName, boolean status);

	@Query("SELECT uf.followingUserName FROM UserFollowing uf WHERE uf.userName = :userName  AND uf.status = true")
	List<String> findFollowingUserNameByUserName(String userName);

	@Query("SELECT uf.userName FROM UserFollowing uf WHERE uf.followingUserName = :userName AND uf.status = true")
	List<String> findFollowersByUserName(@Param("userName") String userName);

	@Query("SELECT uf.followingUserName FROM UserFollowing uf WHERE uf.userName = :userName AND uf.status = true")
	List<String> findFollowingByUserName(@Param("userName") String userName);

	@Query("SELECT uf.followingUserName FROM UserFollowing uf WHERE uf.userName = :userName AND uf.status IS NULL")
	List<String> findRequestByUserName(@Param("userName") String userName);

	@Query("SELECT uf.followingUserName FROM UserFollowing uf WHERE uf.userName = :userName AND uf.followingUserName = :followingUserName AND uf.status IS NULL")
	List<String> findRequestByUserNameandFollowerUsername(@Param("userName") String userName,
			@Param("followingUserName") String followingUserName);

	long countByUserNameAndStatus(String userName, boolean status);

	UserFollowing findByUserNameAndFollowingUserName(String userName, String followingUserName);

	List<String> findByUserNameAndStatusTrue(String userName);

	@Query("SELECT u.userName FROM UserFollowing u WHERE u.followingUserName = :followingUserName AND u.status IS NULL")
	List<String> findUserNameByFollowingUserNameAndStatusIsNull(@Param("followingUserName") String followingUserName);

	@Query("SELECT uf.followingUserName FROM UserFollowing uf WHERE uf.userName = :userName ")
	List<String> findFollowingUserNameByUserName1(String userName);

}
