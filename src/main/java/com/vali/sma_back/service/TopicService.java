package com.vali.sma_back.service;

import com.vali.sma_back.domain.Topic;
import com.vali.sma_back.repository.TopicRepository;
import com.vali.sma_back.service.dto.TopicDTO;
import com.vali.sma_back.service.mapper.TopicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Topic.
 */
@Service
@Transactional
public class TopicService {

    private final Logger log = LoggerFactory.getLogger(TopicService.class);

    private final TopicRepository topicRepository;

    private final TopicMapper topicMapper;

    public TopicService(TopicRepository topicRepository, TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
    }

    /**
     * Save a topic.
     *
     * @param topicDTO the entity to save
     * @return the persisted entity
     */
    public TopicDTO save(TopicDTO topicDTO) {
        log.debug("Request to save Topic : {}", topicDTO);

        Topic topic = topicMapper.toEntity(topicDTO);
        topic = topicRepository.save(topic);
        return topicMapper.toDto(topic);
    }

    /**
     * Get all the topics.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TopicDTO> findAll() {
        log.debug("Request to get all Topics");
        return topicRepository.findAll().stream()
            .map(topicMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * get all the topics where Conversation is null.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TopicDTO> findAllWhereConversationIsNull() {
        log.debug("Request to get all topics where Conversation is null");
        return StreamSupport
            .stream(topicRepository.findAll().spliterator(), false)
            .filter(topic -> topic.getConversation() == null)
            .map(topicMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * get all the topics where Rating is null.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TopicDTO> findAllWhereRatingIsNull() {
        log.debug("Request to get all topics where Rating is null");
        return StreamSupport
            .stream(topicRepository.findAll().spliterator(), false)
            .filter(topic -> topic.getRatings() == null)
            .map(topicMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one topic by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TopicDTO> findOne(Long id) {
        log.debug("Request to get Topic : {}", id);
        return topicRepository.findById(id)
            .map(topicMapper::toDto);
    }

    /**
     * Delete the topic by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Topic : {}", id);
        topicRepository.deleteById(id);
    }

    public List<TopicDTO> getNearbyTopics(Double coordX, Double coordY, Double distance) {
        log.debug("Request to get topics within {}km to ({},{})", distance, coordX, coordY);
        Double radX = coordX * 0.0174533;
        Double radY = coordY * 0.0174533;
        return topicRepository.findLocal(radX, radY, distance)
            .stream()
            .map(topicMapper::toDto)
            .collect(Collectors.toList());
    }
}
