/*
 * Copyright 2015-2017 Jeeva Kandasamy (jkandasa@gmail.com)
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mycontroller.standalone.api.jaxrs.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricsChartDataGroupNVD3 {
    @JsonProperty("chartData")
    private ArrayList<MetricsChartDataNVD3> metricsChartDataNVD3;
    private Integer id;
    private Integer internalId;
    private String variableType;
    private String unit;
    private String unit2;
    private String dataType;
    private String timeFormat;
    private String resourceName;
    private String chartType;
    private String chartInterpolate;
    private Integer marginLeft;
    private Integer marginRight;
    private Integer marginTop;
    private Integer marginBottom;

}