package com.ibm;

import io.kubernetes.client.ApiException;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.*;

import java.util.ArrayList;
import java.util.List;

public class TaskExecutor {
    private String podName;
    private String namespace = "default";
    private String image;
    private String output;
    private CoreV1Api api;

    public TaskExecutor(String podName, String image) {
        this.podName = podName;
        this.image = image;
        this.api = new CoreV1Api();
    }

    public void execute() throws ApiException {
        V1Pod pod = this.buildPod();
        api.createNamespacedPod(this.namespace, pod, null);

        // api.createNamespacedConfigMap("default", null, null);

        V1Pod status = api.readNamespacedPodStatus(this.podName, this.namespace, null);

        String phase = status.getStatus().getPhase();

        this.output = api.readNamespacedPodLog(this.podName, this.namespace, null, null, null, null, null, null, null, null);
    }

    public void destroy() throws ApiException {
        V1DeleteOptions opt = new V1DeleteOptions();

        api.deleteNamespacedPod(this.podName, this.namespace, opt, null, null, null, null);
        api.deleteNamespacedConfigMap(this.podName, this.namespace, opt, null, null
                , null, null);
    }

    public String getOutput() {
        return this.output;
    }
    
    private V1Pod buildPod() {
        V1Pod pod = new V1Pod();
        V1PodSpec podSpec = new V1PodSpec();

        V1ObjectMeta meta = new V1ObjectMeta();
        meta.setName(podName);

        pod.setMetadata(meta);

        podSpec.setRestartPolicy("Never");

        List<V1Container> containers = new ArrayList<V1Container>();
        V1Container container = new V1Container();
        container.setName(image);
        container.setImage(image);
        container.setImagePullPolicy("Always");

        containers.add(container);
        podSpec.setContainers(containers);

        pod.setSpec(podSpec);

        return pod;
    }
}
