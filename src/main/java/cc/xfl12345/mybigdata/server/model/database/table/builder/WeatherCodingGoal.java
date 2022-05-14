package cc.xfl12345.mybigdata.server.model.database.table.builder;

import java.io.Serializable;
import javax.persistence.*;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 表名：weather_coding_goal
*/
@ToString
@SuperBuilder
@Table(name = "`weather_coding_goal`")
public class WeatherCodingGoal implements Serializable {
    /**
     * 当前表所在数据库实例里的全局ID
     */
    @Column(name = "`global_id`")
    private Long globalId;

    /**
     * 天气状况
     */
    @Column(name = "`weather_like`")
    private Long weatherLike;

    /**
     * 摄氏度
     */
    @Column(name = "`celsius_degree`")
    private Integer celsiusDegree;

    private static final long serialVersionUID = 1L;

    /**
     * 获取当前表所在数据库实例里的全局ID
     *
     * @return globalId - 当前表所在数据库实例里的全局ID
     */
    public Long getGlobalId() {
        return globalId;
    }

    /**
     * 设置当前表所在数据库实例里的全局ID
     *
     * @param globalId 当前表所在数据库实例里的全局ID
     */
    public void setGlobalId(Long globalId) {
        this.globalId = globalId;
    }

    /**
     * 获取天气状况
     *
     * @return weatherLike - 天气状况
     */
    public Long getWeatherLike() {
        return weatherLike;
    }

    /**
     * 设置天气状况
     *
     * @param weatherLike 天气状况
     */
    public void setWeatherLike(Long weatherLike) {
        this.weatherLike = weatherLike;
    }

    /**
     * 获取摄氏度
     *
     * @return celsiusDegree - 摄氏度
     */
    public Integer getCelsiusDegree() {
        return celsiusDegree;
    }

    /**
     * 设置摄氏度
     *
     * @param celsiusDegree 摄氏度
     */
    public void setCelsiusDegree(Integer celsiusDegree) {
        this.celsiusDegree = celsiusDegree;
    }
}