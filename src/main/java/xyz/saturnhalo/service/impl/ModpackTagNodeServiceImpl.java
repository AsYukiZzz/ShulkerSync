package xyz.saturnhalo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.saturnhalo.domain.entity.ModpackTagNodeMapping;
import xyz.saturnhalo.repository.ModpackTagNodeRepository;
import xyz.saturnhalo.service.ModpackTagNodeService;

@Service
public class ModpackTagNodeServiceImpl extends ServiceImpl<ModpackTagNodeRepository, ModpackTagNodeMapping> implements ModpackTagNodeService  {

}