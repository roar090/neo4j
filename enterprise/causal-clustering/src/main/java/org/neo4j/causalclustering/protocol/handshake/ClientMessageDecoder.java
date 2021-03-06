/*
 * Copyright (c) 2002-2018 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.causalclustering.protocol.handshake;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.Optional;

import org.neo4j.causalclustering.messaging.marshalling.StringMarshal;

/**
 * Decodes messages received by the client.
 */
public class ClientMessageDecoder extends ByteToMessageDecoder
{
    @Override
    protected void decode( ChannelHandlerContext ctx, ByteBuf in, List<Object> out )
    {
        int messageCode = in.readInt();

        switch ( messageCode )
        {
        case InitialMagicMessage.MESSAGE_CODE:
        {
            String magic = StringMarshal.unmarshal( in );
            out.add( new InitialMagicMessage( magic ) );
            return;
        }
        case 0:
        {
            int statusCodeValue = in.readInt();
            String identifier = StringMarshal.unmarshal( in );
            int version = in.readInt();

            Optional<StatusCode> statusCode = StatusCode.fromCodeValue( statusCodeValue );
            if ( statusCode.isPresent() )
            {
                out.add( new ApplicationProtocolResponse( statusCode.get(), identifier, version ) );
            }
            else
            {
                // TODO
            }
            return;
        }
        case 1:
        {
            // TODO
            out.add( new ModifierProtocolResponse() );
            return;
        }
        case 2:
        {
            int statusCodeValue = in.readInt();
            Optional<StatusCode> statusCode = StatusCode.fromCodeValue( statusCodeValue );
            if ( statusCode.isPresent() )
            {
                out.add( new SwitchOverResponse( statusCode.get() ) );
            }
            else
            {
                // TODO
            }
            return;
        }
        default:
            // TODO
            return;
        }
    }
}
