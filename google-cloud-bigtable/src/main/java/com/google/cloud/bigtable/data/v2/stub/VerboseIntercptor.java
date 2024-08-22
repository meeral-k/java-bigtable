package com.google.cloud.bigtable.data.v2.stub;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener;
import io.grpc.Grpc;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class VerboseIntercptor  implements ClientInterceptor {

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
      CallOptions callOptions, Channel next) {
    final ClientCall<ReqT, RespT> clientCall = next.newCall(method, callOptions);

    return new SimpleForwardingClientCall<ReqT, RespT>(clientCall) {
      @Override
      public void start(Listener<RespT> responseListener, Metadata headers) {
        super.start(
            new SimpleForwardingClientCallListener<RespT>(responseListener) {
              @Override
              public void onHeaders(Metadata headers) {
                // Check peer IP after connection is established.
                SocketAddress remoteAddr =
                    clientCall.getAttributes().get(Grpc.TRANSPORT_ATTR_REMOTE_ADDR);
                System.out.println(String.format("Connected to %s for %s", remoteAddr.toString(), method.getFullMethodName()));
                super.onHeaders(headers);
              }
            },
            headers);
      }
    };
  }
};
  }
}
