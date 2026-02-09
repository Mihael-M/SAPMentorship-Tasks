package sap.mentorship.clouds.controller.metrics;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sap.mentorship.clouds.response.metrics.MetricsResponse;
import sap.mentorship.clouds.service.MetricsService;

@RestController("/clouds/v1/metrics")
public class MetricsController {
    private final MetricsService metricsService;
    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping
    public MetricsResponse getMetrics() {
        return metricsService.getMetrics();
    }
}
