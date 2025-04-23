package org.example.langchain4j;

import com.mongodb.client.result.UpdateResult;
import dev.langchain4j.data.message.ChatMessageSerializer;
import org.example.langchain4j.entity.ChatMessages;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MongoCrudTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testCreateChatMessage() {
        // 创建一个新的 ChatMessages 对象
        ChatMessages chatMessage = new ChatMessages();
        chatMessage.setId(new ObjectId());
        chatMessage.setMemoryId(1);
        chatMessage.setContent("{\"messages\": [\"Hello\", \"World\"]}");

        // 插入数据
        ChatMessages savedMessage = mongoTemplate.save(chatMessage);
        assertNotNull(savedMessage);
        assertNotNull(savedMessage.getId());
    }

    @Test
    public void testReadChatMessage() {

        Criteria criteria = Criteria.where("id").is(new ObjectId("68089cef491fa67e8ab16da5"));

        // 查询数据
        ChatMessages retrievedMessage = mongoTemplate.findOne(new Query(criteria), ChatMessages.class);
        assertNotNull(retrievedMessage);
        System.out.println(retrievedMessage);
    }

    @Test
    public void testUpdateChatMessage() {

        Criteria criteria = Criteria.where("id").is(new ObjectId("68089cef491fa67e8ab16da5"));
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("content", "{\"messages\": [\"Updated\", \"Content\"]}");
        // 使用 upsert 方法，如果不存在则插入新文档
        UpdateResult updatedMessage = mongoTemplate.upsert(query, update, ChatMessages.class);
        assertNotNull(updatedMessage);
    }

    @Test
    public void testDeleteChatMessage() {
        // 创建并保存一个 ChatMessages 对象
        ChatMessages chatMessage = new ChatMessages();
        chatMessage.setId(new ObjectId());
        chatMessage.setMemoryId(1);
        chatMessage.setContent("{\"messages\": [\"Hello\", \"World\"]}");
        ChatMessages savedMessage = mongoTemplate.save(chatMessage);

        // 删除数据
        mongoTemplate.remove(savedMessage);
        ChatMessages deletedMessage = mongoTemplate.findById(savedMessage.getId(), ChatMessages.class);
        assertNull(deletedMessage);
    }
}