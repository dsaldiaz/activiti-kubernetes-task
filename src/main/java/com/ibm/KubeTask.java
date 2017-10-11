package com.ibm;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.util.Config;

import java.io.IOException;


public class KubeTask {
    public static void main(String[] args) throws IOException, ApiException, InterruptedException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);

        TaskExecutor exec = new TaskExecutor("mytest", "busybox");

        try {
            exec.execute();
            exec.getOutput();
        }finally {
             exec.destroy();
        }
    }

}
