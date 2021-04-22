/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.channel;

/**
 * {@link ChannelHandler} which adds callbacks for state changes. This allows the user
 * to hook in to state changes easily.
 */
public interface ChannelInboundHandler extends ChannelHandler {

    /**
     * Channel注册到selector时触发。
     * 客户端在调用connect方法，通过tcp建立连接后，获取SocketChannel后将channel注册在selector时
     * 或者服务端在调用bind方法后创建ServerSocketChannel，通过将channel注册到selector时监听客户端端连接上时被调用到
     * The {@link Channel} of the {@link ChannelHandlerContext} was registered with its {@link EventLoop}
     */
    void channelRegistered(ChannelHandlerContext ctx) throws Exception;

    /**
     * channel取消注册到selector时被调用到，通常在channel关闭时触发
     * 首先触发channelInactive事件，然后触发channelUnregistered事件
     * The {@link Channel} of the {@link ChannelHandlerContext} was unregistered from its {@link EventLoop}
     */
    void channelUnregistered(ChannelHandlerContext ctx) throws Exception;

    /**
     * channel处于激活的事件，在netty中，处于激活状态表示底层socket的isOpen isConnected 返回true
     * The {@link Channel} of the {@link ChannelHandlerContext} is now active
     */
    void channelActive(ChannelHandlerContext ctx) throws Exception;

    /**
     * channel处于非激活，调用close方法会触发该事件，然后触发channelUnregistered事件
     * The {@link Channel} of the {@link ChannelHandlerContext} was registered is now inactive and reached its
     * end of lifetime.
     */
    void channelInactive(ChannelHandlerContext ctx) throws Exception;

    /**
     * channel从对端读取数据，当事件轮询到读事件，调用底层socketChannel的read方法后，将读取的字节通过事件链进行处理
     * nio的触发入口为AbstractNioByteChannel的内部类NioByteUnsafe的read方法
     * Invoked when the current {@link Channel} has read a message from the peer.
     */
    void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception;

    /**
     * 处理完一次channel读事件后触发，在netty中一次读事件处理，会多次调用socketChannel的read方法
     * 触发入口为AbstractNioByteChannel的内部类NioByteUnsafe的read方法
     * Invoked when the last message read by the current read operation has been consumed by
     * {@link #channelRead(ChannelHandlerContext, Object)}.  If {@link ChannelOption#AUTO_READ} is off, no further
     * attempt to read an inbound data from the current {@link Channel} will be made until
     * {@link ChannelHandlerContext#read()} is called.
     */
    void channelReadComplete(ChannelHandlerContext ctx) throws Exception;

    /**
     * 触发用户自定义的事件
     * TODO ChannelInboundShutdownEvent 半关闭(输入端关闭而服务端不关闭)
     * Gets called if an user event was triggered.
     */
    void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception;

    /**
     * netty写缓冲区可写状态变更事件(可写 -> 不可写  -> 可写)
     * Gets called once the writable state of a {@link Channel} changed. You can check the state with
     * {@link Channel#isWritable()}.
     */
    void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception;

    /**
     * 异常事件
     * Gets called if a {@link Throwable} was thrown.
     */
    @Override
    @SuppressWarnings("deprecation")
    void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception;
}
