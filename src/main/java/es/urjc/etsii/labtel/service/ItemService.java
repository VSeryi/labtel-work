package es.urjc.etsii.labtel.service;

import es.urjc.etsii.labtel.domain.Item;
import es.urjc.etsii.labtel.domain.enumeration.TypeItem;
import es.urjc.etsii.labtel.repository.ItemRepository;
import es.urjc.etsii.labtel.service.dto.ItemDTO;
import es.urjc.etsii.labtel.service.mapper.ItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Item.
 */
@Service
@Transactional
public class ItemService {

    private final Logger log = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    /**
     * Save a item.
     *
     * @param itemDTO the entity to save
     * @return the persisted entity
     */
    public ItemDTO save(ItemDTO itemDTO) {
        log.debug("Request to save Item : {}", itemDTO);

        Item item = itemMapper.toEntity(itemDTO);
        item = itemRepository.save(item);
        return itemMapper.toDto(item);
    }

    /**
     * Get all the items.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Items");
        return itemRepository.findAll(pageable)
            .map(itemMapper::toDto);
    }
    
    /**
     * Get all the items.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ItemDTO> findAllByType(TypeItem type, Pageable pageable) {
        log.debug("Request to get all Items");
        return itemRepository.findAllByType(type, pageable)
            .map(itemMapper::toDto);
    }

    /**
     * Get all the Item with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<ItemDTO> findAllWithEagerRelationships(Pageable pageable) {
        return itemRepository.findAllWithEagerRelationships(pageable).map(itemMapper::toDto);
    }
    
    /**
     * Get all the Item with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<ItemDTO> findAllByTypeWithEagerRelationships(TypeItem type, Pageable pageable) {
        return itemRepository.findAllByTypeWithEagerRelationships(type, pageable).map(itemMapper::toDto);
    }
    

    /**
     * Get one item by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ItemDTO> findOne(Long id) {
        log.debug("Request to get Item : {}", id);
        return itemRepository.findOneWithEagerRelationships(id)
            .map(itemMapper::toDto);
    }

    /**
     * Delete the item by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Item : {}", id);
        itemRepository.deleteById(id);
    }
}
