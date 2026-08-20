/*
Copyright 2023 Structure Projects

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package cn.structured.security.common.bo.risk;

import lombok.Data;

import java.util.List;

/**
 * 风险检测BO
 *
 * @author chuck
 * @since 2024/7/17
 */
@Data
public class RiskResultBO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 风险结果
     */
    private String riskResult;

    /**
     * 风险评分
     */
    private String riskScore;

    /**
     * 风险内容集合
     */
    private List<RiskResultItem> resultItems;



}
