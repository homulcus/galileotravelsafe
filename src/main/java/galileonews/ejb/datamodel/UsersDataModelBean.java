/*
 * Copyright 2015 Samuel Franklyn <sfranklyn@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package galileonews.ejb.datamodel;

import javax.ejb.Stateless;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class UsersDataModelBean extends BaseDataModelBean {

    public static final String SELECT_ALL = "Users.selectAll";
    public static final String SELECT_ALL_COUNT = "Users.selectAllCount";
    public static final String SELECT_LIKE_USERNAME = "Users.selectLikeUserName";
    public static final String SELECT_LIKE_USERNAME_COUNT = "Users.selectLikeUserNameCount";
}
